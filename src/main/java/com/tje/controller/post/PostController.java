package com.tje.controller.post;


import com.tje.controller.contact.Contact;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

// GET /posts
// 게시글 목록이 JSON으로 나오게
@RestController
@RequestMapping("/posts")
public class PostController {
    // 동시성을 위한 자료 구조
    // HashMap -> ConcurrentHashMap
    // Integer -> AtomicInteger
    Map<Integer, Post> map = new ConcurrentHashMap<>();
    AtomicInteger num = new AtomicInteger(0);

    // post 목록 화면을 제작 post.html, post.js
    // fetch api를 사용하여 /posts 주소를 호출한 후
    // 배열 목록을 div(카드)로 표시한다.

//    -----------------
//    | 게시자         |
//    | ------------- |
//    | 제목(h3)       |
//    | 본문(p)        |
//    |  .....        |
//    |  .....        |
//    | ------------- |
//    | 생성시간       |
//    -----------------

    @GetMapping
    public List<Post> getPostList() {
//        // 증가시키고 값 가져오기
//        int no = num.incrementAndGet();
//        map.put(no, Post.builder()
//                        .no(no)
//                        .creatorName("홍길동")
//                        .title("1Lorem, ipsum dolor.")
//                        .content("Lorem ipsum dolor sit amet, consectetur adipisicing elit. Repudiandae maiores sunt ab beatae provident? Eius non accusantium vitae dolor ipsa!")
//                        .createdTime(new Date().getTime())
//                        .build());
//
//       int no = num.incrementAndGet();
//        map.put(no, Post.builder()
//                        .no(no)
//                        .creatorName("홍길동")
//                        .title("2Lorem, ipsum dolor.")
//                        .content("Lorem ipsum dolor sit amet, consectetur adipisicing elit. Repudiandae maiores sunt ab beatae provident? Eius non accusantium vitae dolor ipsa!")
//                        .createdTime(new Date().getTime())
//                        .build());
//        map.put(no, Post.builder()
//                        .no(no)
//                        .creatorName("양수열")
//                        .title("")
//                        .content("")
//                        .createdTime(new Date().getTime())
//                        .build());

        // 람다식(lambda expression)
        // 식이 1개인 함수식;
        // 매개변수 영역과 함수 본체를 화살표로 구분함
        // 함수 본체의 수식 값이 반환 값

        List<Post> list = new ArrayList<>(map.values());
        list.sort((a,b)-> (int)(b.getNo() - a.getNo()));
        return list;
    }
    @PostMapping
    public ResponseEntity<Map<String, Object>> addContact(@RequestBody Post post) {
        // 클라이언트에서 넘어온 JSON이 객체로 잘 변환됐는지 확인
        // 이메일 필수값 검증
        // 400: bad request
//        System.out.println(post.getCreatorName());
//        System.out.println(post.getCreatedTime());
//        System.out.println(post.getNo());

        int no = num.incrementAndGet();
        post.setNo(no); // 'no' 속성을 map에 추가하기 전에 설정합니다.
        post.setCreatorName("양수열");
        post.setCreatedTime(new Date().toString());
        map.put(no, post); // 'no'를 키로 하여 게시물을 저장합니다.

        if(post.getCreatorName() == null || post.getCreatorName().isEmpty()) {
            // 응답 객체 생성
            Map<String, Object> res = new HashMap<>();
            res.put("data", null);
            res.put("message", "[creatorName] field is required");

            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(res);
        }


        // 응답 객체 생성
        // 실제로 생성된 객체를 응답
        Map<String, Object> res = new HashMap<>();
        res.put("data", post);
        res.put("message", "created");

        // HTTP Status Code: 201 Created
        // 리소스가 정상적으로 생성되었음
        System.out.println(post);
        return ResponseEntity.status(HttpStatus.CREATED).body(res);

    }
        //    -- 받는 정보
//    title, content
//    -> null 또는 없으면 bad-request 응답




//    -- 서버에 생성
//    no = num.incrementAndGet();
//    creatorName = "John Doe"
//    createdTime = new Date().getTime()

    // title, content 필수 속성
    // creatorName: 서버에서 가짜데이터로 넣음
//    @PostMapping
//    public ResponseEntity<Map<String, Object>> addPost(...) {
    // 1. 입력값 검증(title, content)
    //  -> 입력값 오류(빈 값)가 있으면 400 에러 띄움

    // 2. 채번: 번호를 딴다(1 .. 2, 3....)
    //    int no = num.incrementAndGet();

    // 3. 번호(no), 시간값(createdTime) 게시자이름(creatorName) 요청 객체에 설정
    // (set필드명(...))

    // 4. 맵에 추가

    // 5. 생성된 객체를 맵에서 찾아서 반환, 201
//    }
}