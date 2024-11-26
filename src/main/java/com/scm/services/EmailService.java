package com.scm.services;

import java.io.File;

public interface EmailService {
    //email to single person
    void sendEmail(String to, String subject, String body);

    //email to multiple person
    void sendEmail(String[] to, String subject, String body);

    //email with html
    void sendEmailWithHtml(String to, String subject, String htmlContent);

    //email with file
    void sendEmailWithFile(String to, String subject, String body, File file);
}