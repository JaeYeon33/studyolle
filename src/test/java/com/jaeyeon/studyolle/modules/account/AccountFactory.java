package com.jaeyeon.studyolle.modules.account;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccountFactory {

    @Autowired AccountRepository accountRepository;

    public Account createAccount(String nickname) {
        Account sergey = new Account();
        sergey.setNickname(nickname);
        sergey.setEmail(nickname);
        sergey.setEmail(nickname + "@email.com");
        accountRepository.save(sergey);
        return sergey;
    }

}
