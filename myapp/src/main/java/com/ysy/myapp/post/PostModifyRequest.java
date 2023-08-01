package com.ysy.myapp.post;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
@Data
@NoArgsConstructor
public class PostModifyRequest {
    private String title;
    private String creatorName;
}
