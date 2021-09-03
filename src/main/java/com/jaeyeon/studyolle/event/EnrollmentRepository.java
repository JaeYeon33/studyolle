package com.jaeyeon.studyolle.event;

import com.jaeyeon.studyolle.domain.Account;
import com.jaeyeon.studyolle.domain.Enrollment;
import com.jaeyeon.studyolle.domain.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    boolean existsByEventAndAccount(Event event, Account account);

    Enrollment findByEventAndAccount(Event event, Account account);
}
