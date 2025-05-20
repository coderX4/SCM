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

    @Value("${scm.mailer.id}")
    private String mailerId;
    @Value("${scm.mailer.key}")
    private String mailerKey;

    //verification mailer from the scm app
    @Override
    public void sendEmail(String to, String subject, String body) {

        JavaMailSender mailSender = dynamicMailSender.getMailSender(
                mailerId, mailerKey
        );

        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(mailerId);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }

    @Override
    public void sendEmailToUser(String from,String to, String subject, String body,String password) {

        JavaMailSender mailSender = dynamicMailSender.getMailSender(
                from, password
        );

        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(from);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }
}
