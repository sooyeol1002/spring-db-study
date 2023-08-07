package com.ysy.myapp.auth.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
public @interface Auth {
    // 역할(일반사용자, 골드사용자, 관리자, 판매관리자)
    public boolean require() default true;
}
