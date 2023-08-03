package com.ysy.myapp.auth;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor // 빌더를 사용하려면 필요
@Builder
@Entity
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false)
    private String nickname;
    private String email;

    // 관계 맵핑
    @OneToOne
    // profile_id 컬럼이 FK로 생성됨
    private Login login;
}
