package com.tje.controller.post;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Post {
    private long no; // (key)번호
    private String content; // 내용
    private String creatorName; // 게시자
    private String title; // 제목
    private long createdTime; // 생성시간
}
