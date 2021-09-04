package com.jaeyeon.studyolle.modules.account;


import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.List;

/**
 * SpringSecurity 가 다루는 유저정보와
 * Domain 에서 다루는 유저정보를 연결해주는 어댑터 역할
 */
@Getter
public class UserAccount extends User {

    // currentUser account 와 일치
    private Account account;

    public UserAccount(Account account) {
        super(account.getNickname(), account.getPassword(), List.of(new SimpleGrantedAuthority("ROLE_USER")));
        this.account = account;
    }
}
