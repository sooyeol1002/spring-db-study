package com.tje.controller.post;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Post {
    private long no; // (key)번호
    private String title; // 제목
    private String creatorName; // 게시자
    private String content; // 내용
    private String createdTime; // 생성시간
}


