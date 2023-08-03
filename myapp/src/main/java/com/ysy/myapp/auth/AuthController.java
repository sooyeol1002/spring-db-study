package com.ysy.myapp.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

//    @GetMapping(value = "/logins")
//    public List<Login> getLogins() {
//        return repo.findAll();
//    }
    @Autowired
    private LoginRepository repo;

    @Autowired
    private AuthService service;

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
}
