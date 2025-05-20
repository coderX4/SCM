package com.scm.services;

public interface EmailService {
    //email to single person
    void sendEmail(String to, String subject, String body);

    void sendEmailToUser(String from,String to, String subject, String body,String password);
}