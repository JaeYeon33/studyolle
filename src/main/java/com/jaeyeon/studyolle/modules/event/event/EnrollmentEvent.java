package com.jaeyeon.studyolle.modules.event.event;

import com.jaeyeon.studyolle.modules.event.Enrollment;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public abstract class EnrollmentEvent {

    protected final Enrollment enrollment;
    protected final String message;
}
