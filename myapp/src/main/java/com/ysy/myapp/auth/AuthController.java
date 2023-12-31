package com.ysy.myapp.auth;

import com.ysy.myapp.auth.entity.Login;
import com.ysy.myapp.auth.entity.LoginRepository;
import com.ysy.myapp.auth.entity.Profile;
import com.ysy.myapp.auth.entity.ProfileRepository;
import com.ysy.myapp.auth.request.SignupRequest;
import com.ysy.myapp.auth.util.HashUtil;
import com.ysy.myapp.auth.util.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private LoginRepository repo;

    @Autowired
    private ProfileRepository profileRepo;

    @Autowired
    private AuthService service;

    // AuthController와 HashUtil은 중간수준의 결합도(coupling)
    // HashUtil 객체를 메서드에서 생성 높은수준의 결합도(coupling)
    @Autowired
    private HashUtil hash;

    @Autowired
    private JwtUtil jwt;

//    @GetMapping(value = "/logins")
//    public List<Login> getLogins() {
//        return repo.findAll();
//    }

    @PostMapping(value = "/signup")
    public ResponseEntity signup(@RequestBody SignupRequest req) {
        // 1. volidation
        // 입력값 검증
        // 사용자이름없거나/중복이거나, 패스워드없거나, 닉네임, 이메일 없음...

        // 2. Buisness Logic(데이터 처리)
        // profile, login 생성 트랜젝션 처리
        long profileId = service.createIdentity(req);

        // 3. Response
        // 201: created
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    //1. (브라우저) 로그인 요청
    // [RequestLine]
    //   HTTP 1.1 POST 로그인주소
    // [RequestHeader]
    //   content-type: www-form-urlencoded
    // [Body]
    //   id=...&pw=...

    //2. (서버) 로그인 요청을 받고 인증처리 후 쿠키 응답 및 웹페이지로 이동
    // HTTP Status 302 (리다이렉트)
    // [Response Header]
    //   Set-Cookie: 인증키=키........; domain=.naver.com
    //   Location: "리다이렉트 주소"

    //3. (브라우저) 쿠키를 생성(도메인에 맞게)

    @PostMapping(value = "/signin")
    public ResponseEntity signIn(
            @RequestParam String username,
            @RequestParam String password,
            HttpServletResponse res) {
        System.out.println(username);
        System.out.println(password);
        // 1. username, pw 인증 확인
        //   1.1 username으로 login테이블에서 조회후 id, secret까지 조회
        Optional<Login> login = repo.findByUsername(username);
        // username에 매칭이 되는 레코드가 없는 상태
        if(!login.isPresent()) {
            // 401 Unauthorized
            // 클라이언트에서 대충 뭉뜨그려서 [인증정보가 잘못되었습니다.]
            // [사용자이름 또는 패스워드가 잘못되었습니다.]
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        //   1.2 password+salt -> 해시 -> secret 일치여부 확인
        //   1.3 일치하면 다음코드를 실행
        //   1.4 일치하지 않으면 401 Unauthorized 반환 종료
        boolean isVerified = hash.verifyHash(password, login.get().getSecret());
//        System.out.println("verified: " + isVerified);

        if(!isVerified) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Login l = login.get();
        // 2. profile 정보를 조회하여 인증키 생성(JWT)
        Optional<Profile> profile = profileRepo.findByLogin_Id(l.getId());
        // 로그인정보와 프로필 정보가 제대로 연결 안됨.
        if(!profile.isPresent()) {
            // 409 conflict: 데이터 현재 상태가 안 맞음
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        String token = jwt.createToken(
                l.getId(), l.getUsername(),
                profile.get().getNickname());
        System.out.println(token);

        // 3. cookie와 헤더를 생성한후 리다이렉트
        Cookie cookie = new Cookie("token", token);
        cookie.setPath("/");
        cookie.setMaxAge((int) (jwt.TOKEN_TIMEOUT / 1000L)); // 만료시간
        cookie.setDomain("localhost"); // 쿠키를 사용할 수 있 도메인

        // 응답헤더에 쿠키 추가
        res.addCookie(cookie);

        // 웹 첫페이지로 리다이렉트
        return ResponseEntity
                .status(HttpStatus.FOUND)
                .location(ServletUriComponentsBuilder
                        .fromHttpUrl("http://localhost:5500")
                        .build().toUri())
                .build();
    }
}