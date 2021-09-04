package com.jaeyeon.studyolle.infra.mail;


public interface EmailService {

    void sendEmail(EmailMessage emailMessage);
}
