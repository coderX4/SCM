package com.scm.services;

import java.io.File;

public interface EmailService {
    //email to single person
    void sendEmail(String to, String subject, String body);

    void sendEmailToUser(String from,String to, String subject, String body,String password);

    //email to multiple person
    void sendEmailToUsers(String[] to, String subject, String body);

    //email with html
    void sendEmailWithHtml(String to, String subject, String htmlContent);

    //email with file
    void sendEmailWithFile(String to, String subject, String body, File file);
}