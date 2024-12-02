package com.scm.services.impl;

import com.scm.config.DynamicMailSender;
import com.scm.entity.User;
import com.scm.services.EmailService;
import com.scm.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
@Slf4j
public class EmailServiceImpl implements EmailService {

    @Autowired
    private DynamicMailSender dynamicMailSender;

    //verification mailer from the scm app
    @Override
    public void sendEmail(String to, String subject, String body) {

        JavaMailSender mailSender = dynamicMailSender.getMailSender(
                "smartcmapp@gmail.com",
                "llcebyfaxqhxkizf"
        );

        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom("smartcmapp@gmail.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        log.info("Sending email to: " + to);
        mailSender.send(message);
    }

    @Override
    public void sendEmailToUser(String from,String to, String subject, String body,String password) {

        JavaMailSender mailSender = dynamicMailSender.getMailSender(
                from, password
                //"cqrsjjejirmyyydu"
        );

        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(from);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        log.info("Sending email from: " + from);
        log.info("Sending email to: " + to);
        mailSender.send(message);
    }

    @Override
    public void sendEmailToUsers(String[] to, String subject, String body) {

    }

    @Override
    public void sendEmailWithHtml(String to, String subject, String htmlContent) {

    }

    @Override
    public void sendEmailWithFile(String to, String subject, String body, File file) {

    }
}
