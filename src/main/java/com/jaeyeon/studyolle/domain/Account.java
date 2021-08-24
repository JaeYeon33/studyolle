package com.jaeyeon.studyolle.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter @Setter @EqualsAndHashCode(of = "id")
@Builder @AllArgsConstructor @NoArgsConstructor
public class Account {

    @Id @GeneratedValue
    private Long id;

    @Column(unique = true) // 중복된 것을 허용하지 않기 때문에 unique 설정
    private String email;

    @Column(unique = true) // 중복된 것을 허용하지 않기 때문에 unique 설정
    private String nickname;

    private String password;

    private boolean emailVerified;
    private String emailCheckToken; // 이메일 검증 토큰값
    private LocalDateTime emailCheckTokenGeneratedAt;
    private LocalDateTime joinedAt; // 인증 거친 사용자 가입기록

    // 프로필 관련 정보
    private String bio;
    private String url;
    private String occupation;
    private String location;

    @Lob @Basic(fetch = FetchType.EAGER) // String = varchar(255) 이지만 더 커질 수 있기 때문에 @Lob, 기본 Lazy --> EAGER
    private String profileImage;

    // 알림
    private boolean studyCreatedByEmail;
    private boolean studyCreatedByWeb;
    private boolean studyEnrollmentResultByEmail;
    private boolean studyEnrollmentResultByWeb;
    private boolean studyUpdatedByEmail;
    private boolean studyUpdatedByWeb;

    public void generateEmailCheckToken() {
        this.emailCheckToken = UUID.randomUUID().toString();
        this.emailCheckTokenGeneratedAt = LocalDateTime.now();
    }

    public void completeSignUp() {
        this.emailVerified = true;
        this.joinedAt = LocalDateTime.now();
    }

    public boolean isValidToken(String token) {
        return this.emailCheckToken.equals(token);
    }

    public boolean canSendConfirmEmail() {
        return this.emailCheckTokenGeneratedAt.isBefore(LocalDateTime.now().minusHours(1));
    }
}
