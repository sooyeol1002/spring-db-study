package com.ysy.myapp.post;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

// GET /posts
// 게시글 목록이 JSON으로 나오게
@RestController
@RequestMapping("/posts")
public class PostController {
    // 동시성을 위한 자료 구조
    // HashMap -> ConcurrentHashMap
    // Integer -> AtomicInteger
//    Map<Long, Post> map = new ConcurrentHashMap<>();
//    AtomicLong num = new AtomicLong(0);

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

    @Autowired
    PostRepository repo;

    @GetMapping
    public List<Post> getPostList() {
//        // 증가시키고 값 가져오기
//        long no = num.incrementAndGet();
//        map.put(no, Post.builder()
//                        .no(no)
//                        .creatorName("홍길동")
//                        .title("1Lorem, ipsum dolor.")
//                        .content("Lorem ipsum dolor sit amet, consectetur adipisicing elit. Repudiandae maiores sunt ab beatae provident? Eius non accusantium vitae dolor ipsa!")
//                        .createdTime(new Date().getTime())
//                        .build());
//
//        no = num.incrementAndGet();
//        map.put(no, Post.builder()
//                        .no(no)
//                        .creatorName("홍길동")
//                        .title("2Lorem, ipsum dolor.")
//                        .content("Lorem ipsum dolor sit amet, consectetur adipisicing elit. Repudiandae maiores sunt ab beatae provident? Eius non accusantium vitae dolor ipsa!")
//                        .createdTime(new Date().getTime())
//                        .build());

//        var list = new ArrayList<>(map.values());
        // 람다식(lambda expression)
        // 식이 1개인 함수식;
        // 매개변수 영역과 함수 본체를 화살표로 구분함
        // 함수 본체의 수식 값이 반환 값
//        list.sort((a,b)-> (int)(b.getNo() - a.getNo()));

        List<Post> list = repo.findAllByOrderByNo();
        return list;
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
    @PostMapping
    public ResponseEntity<Map<String, Object>> addPost(@RequestBody Post post) {
//     1. 입력값 검증(title, content)
//        System.out.println(post.getNo());
//        System.out.println(post.getTitle());
//        System.out.println(post.getCreatorName());
//        System.out.println(post.getCreatedTime());

//      -> 입력값 오류(빈 값)가 있으면 400 에러 띄움
        if (post.getTitle() == null || post.getContent() == null || post.getContent().isEmpty() || post.getTitle().isEmpty()) {
            Map<String, Object> response = new HashMap<>();
            response.put("data", null);
            response.put("message", "[title] and [content] is Required Field");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

//     2. 채번1 .. 2, 3....)
        // 데이터베이스의 auto_increment를 사용할 것이므로
        // 아래 2줄은 필요없게 된다. AtomicLong 필요없음.
//        long no = num.incrementAndGet();
//        post.setNo(no);


//     3. 번호(no), 시간값(createdTime) 게시자이름(creatorName) 요청 객체에 설정
        post.setCreatedTime(new Date().getTime());

        System.out.println(post.getCreatedTime());
//     5. 생성된 객체를 맵에서 찾아서 반환, 201
        Post savedPost = repo.save(post);
        System.out.println(savedPost);
        if (savedPost != null) {
            Map<String, Object> res = new HashMap<>();
            res.put("data", savedPost);
            res.put("message", "created");
            return ResponseEntity.status(HttpStatus.CREATED).body(res);
        }
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(value = "/{no}")
    public ResponseEntity removePost(@PathVariable long no) {
        System.out.println(no);

        if (!repo.findById(no).isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        repo.deleteById(no);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PutMapping(value = "/{no}")
    public ResponseEntity modifyPost(@PathVariable long no, @RequestBody PostModifyRequest post) {
        System.out.println(no);
        System.out.println(post);

        Optional<Post> findedPost = repo.findById(no);

        if (!findedPost.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Post toModifyPost = findedPost.get();

        if (post.getTitle() != null && !post.getTitle().isEmpty()) {
            toModifyPost.setTitle(post.getTitle());
        }
        if (post.getCreatorName() != null && !post.getCreatorName().isEmpty()) {
            toModifyPost.setCreatorName(post.getCreatorName());
        }
        repo.save(toModifyPost);

        return ResponseEntity.ok().build();
    }
}