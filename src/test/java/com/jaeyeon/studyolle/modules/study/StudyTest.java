package com.jaeyeon.studyolle.modules.study;

import com.jaeyeon.studyolle.modules.account.Account;
import com.jaeyeon.studyolle.modules.account.UserAccount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StudyTest {

    Study study;
    Account account;
    UserAccount userAccount;

    @BeforeEach
    void beforeEach() {
        study = new Study();
        account = new Account();
        account.setNickname("jaeyeon");
        account.setPassword("12345678");
        userAccount = new UserAccount(account);
    }

    @Test
    @DisplayName("스터디를 공개했고 인원 모집 중이고, 이미 멤버나 스터디 관리자가 아니라면 스터디 가입 가능")
    void isJoinable() throws Exception {
        study.setPublished(true);
        study.setRecruiting(true);

        assertTrue(study.isJoinable(userAccount));
    }

    @Test
    @DisplayName("스터디를 공개했고 인원 모집 중이더라도, 스터디 관리자는 스터디 가입이 불필요하다.")
    void isJoinable_false_for_manager() throws Exception {
        study.setPublished(true);
        study.setRecruiting(true);
        study.addManager(account);

        assertFalse(study.isJoinable(userAccount));
    }

    @Test
    @DisplayName("스터디가 비공개거나 인원 모집 중이 아니면 스터디 가입이 불가능하다.")
    void isJoinable_false_for_non_recruiting_study() throws Exception {
        study.setPublished(true);
        study.setRecruiting(false);

        assertFalse(study.isJoinable(userAccount));

        study.setPublished(false);
        study.setRecruiting(false);

        assertFalse(study.isJoinable(userAccount));
    }

    @Test
    @DisplayName("스터디 관리자 인지 확인")
    void isManager() throws Exception {
       study.addManager(account);
       assertTrue(study.isManager(userAccount));
    }

    @Test
    @DisplayName("스터디 멤버인지 확인")
    void isMember() throws Exception {
        study.addMember(account);
        assertTrue(study.isMember(userAccount));
    }
}
