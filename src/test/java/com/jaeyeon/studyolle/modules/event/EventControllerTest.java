package com.jaeyeon.studyolle.modules.event;

import com.jaeyeon.studyolle.infra.MockMvcTest;
import com.jaeyeon.studyolle.modules.account.Account;
import com.jaeyeon.studyolle.modules.account.AccountFactory;
import com.jaeyeon.studyolle.modules.account.AccountRepository;
import com.jaeyeon.studyolle.modules.account.WithAccount;
import com.jaeyeon.studyolle.modules.study.Study;
import com.jaeyeon.studyolle.modules.study.StudyFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import javax.persistence.EnumType;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@MockMvcTest
class EventControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired StudyFactory studyFactory;
    @Autowired AccountFactory accountFactory;
    @Autowired EventService eventService;
    @Autowired EnrollmentRepository enrollmentRepository;
    @Autowired AccountRepository accountRepository;

    @Test
    @DisplayName("선착순 모임에 참가 신청 - 자동 수락")
    @WithAccount("jaeyeon")
    void newEnrollment_to_FCFS_event_accepted() throws Exception {
        Account sergey = accountFactory.createAccount("sergey");
        Study study = studyFactory.createStudy("test-stduy", sergey);
        Event event = createEvent("test-event", EventType.FCFS, 2, study, sergey);

        mockMvc.perform(post("/study/" + study.getPath() + "/events/" + event.getId() + "/enroll")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/study/" + study.getEncodedPath() + "/events/" + event.getId()));

        Account jaeyeon = accountRepository.findByNickname("jaeyeon");
        isAccepted(jaeyeon, event);

    }

    @Test
    @DisplayName("선착순 모임에 참가 신청 - 대기중 (이미 인원이 꽉차서)")
    @WithAccount("jaeyeon")
    void newEnrollment_to_FCFS_event_not_accepted() throws Exception {
        Account sergey = accountFactory.createAccount("sergey");
        Study study = studyFactory.createStudy("test-study", sergey);
        Event event = createEvent("test-event", EventType.FCFS, 2, study, sergey);

        Account tim = accountFactory.createAccount("Tim");
        Account craig = accountFactory.createAccount("Craig");
        eventService.newEnrollment(event, tim);
        eventService.newEnrollment(event, craig);

        mockMvc.perform(post("/study/" + study.getPath() + "/events/" + event.getId() + "/enroll")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/study/" + study.getEncodedPath() + "/events/" + event.getId()));

        Account jaeyeon = accountRepository.findByNickname("jaeyeon");
        isNotAccepted(jaeyeon, event);
    }

    @Test
    @DisplayName("참가신청 확정자가 선착순 모임에 참가 신청을 취소하는 경우, 바로 다음 대기자를 자동으로 신청 확인한다.")
    @WithAccount("jaeyeon")
    void accepted_account_cancelEnrollment_to_FCFS_event_not_accepted() throws Exception {
        Account jaeyeon = accountRepository.findByNickname("jaeyeon");
        Account sergey = accountFactory.createAccount("sergey");
        Account may = accountFactory.createAccount("may");
        Study study = studyFactory.createStudy("test-study", sergey);
        Event event = createEvent("test-event", EventType.FCFS, 2, study, sergey);

        eventService.newEnrollment(event, may);
        eventService.newEnrollment(event, jaeyeon);
        eventService.newEnrollment(event, sergey);

        isAccepted(may, event);
        isAccepted(jaeyeon, event);
        isNotAccepted(sergey, event);

        mockMvc.perform(post("/study/" + study.getPath() + "/events/" + event.getId() + "/disenroll")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/study/" + study.getEncodedPath() + "/events/" + event.getId()));

        isAccepted(may, event);
        isAccepted(sergey, event);
        assertNull(enrollmentRepository.findByEventAndAccount(event, jaeyeon));
    }

    @Test
    @DisplayName("참가신청 비확정자가 선착순 모임에 참가 신청을 취소하는 경우, 기존 확정자를 그대로 유지하고 새로운 확정자는 없다 ")
    @WithAccount("jaeyeon")
    void not_accepted_account_cancelEnrollment_to_FCFS_event_not_accepted() throws Exception {
        Account jaeyeon = accountRepository.findByNickname("jaeyeon");
        Account sergey = accountFactory.createAccount("sergey");
        Account may = accountFactory.createAccount("may");
        Study study = studyFactory.createStudy("test-study", sergey);
        Event event = createEvent("test-event", EventType.FCFS, 2, study, sergey);

        eventService.newEnrollment(event, may);
        eventService.newEnrollment(event, sergey);
        eventService.newEnrollment(event, jaeyeon);

        isAccepted(may, event);
        isAccepted(sergey, event);
        isNotAccepted(jaeyeon, event);

        mockMvc.perform(post("/study/" + study.getPath() + "/events/" + event.getId() + "/disenroll")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/study/" + study.getPath() + "/events/" + event.getId()));

        isAccepted(may, event);
        isAccepted(sergey, event);
        assertNull(enrollmentRepository.findByEventAndAccount(event, jaeyeon));
    }

    private void isNotAccepted(Account sergey, Event event) {
        assertFalse(enrollmentRepository.findByEventAndAccount(event, sergey).isAccepted());
    }

    private void isAccepted(Account account, Event event) {
        assertTrue(enrollmentRepository.findByEventAndAccount(event, account).isAccepted());
    }


    private Event createEvent(String eventTitle, EventType eventType, int limit, Study study, Account account) {
        Event event = new Event();
        event.setEventType(eventType);
        event.setLimitOfEnrollments(limit);
        event.setTitle(eventTitle);
        event.setCreatedDateTime(LocalDateTime.now());
        event.setEndEnrollmentDateTime(LocalDateTime.now().plusDays(1));
        event.setStartDateTime(LocalDateTime.now().plusDays(1).plusHours(1));
        event.setEndDateTime(LocalDateTime.now().plusDays(1).plusHours(7));
        return eventService.createEvent(event, study, account);
    }

}