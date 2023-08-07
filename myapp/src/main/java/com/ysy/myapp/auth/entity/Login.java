package com.ysy.myapp.auth.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Data
@NoArgsConstructor // ? 필수라는데 왜지
@AllArgsConstructor // 빌더를 사용하려면 필요
@Builder
@Entity
public class Login {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ?
    private long id;
    // candidate key(후보키, pk가 가능한 키, 유일성+대표성)
    // 시스템의 PK로 사용적합X
    // clustered-index로 사용하기가 부적합
    @Column(unique = true)
    private String username;

    @Column(length = 500)
    private String secret;

    // EAGER: JPA 메서드 실행하면 바로 관계 테이블 조회
    // LAZY: 관계 테이블 객체를 엑세스 시점에 SQL 실행해서 조회
    // @OneToOne(fetch = FetchType.LAZY) // ?
    // profile_id의 forein key가 생성
    // @JsonIgnore
    // JSON으로 출력할 때 처리 안 함

    // 관계테이블에 키값만 저장
    private long profileId;
}
