package com.scm.controllers;

import com.scm.entity.Contact;
import com.scm.entity.User;
import com.scm.forms.ContactSearchForm;
import com.scm.forms.MailForm;
import com.scm.helper.AppConstants;
import com.scm.helper.Message;
import com.scm.helper.MessageType;
import com.scm.services.ContactService;
import com.scm.services.EmailService;
import com.scm.services.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping({"/user/mails"})
public class MailController {
    @Autowired
    private EmailService emailService;

    @Autowired
    private ContactService contactService;

    @Autowired
    private UserService userService;

    @GetMapping
    public String mailFirst(){
        return "user/mails/start_with_mails";
    }

    @GetMapping(("/start-mails/{userMail}"))
    public String mailResolver(@PathVariable("userMail") String userMail){
        User user = userService.getUserByEmail(userMail);
        String apppassword = user.getPassKeyForMail();
        if(apppassword != null){
            return "redirect:/user/mails/mail-view/"+userMail;
        }
        else{
            return "redirect:/user/mails";
        }
    }

    @PostMapping({"/save-app-password/{userMail}"})
    public String SaveAppPassword(@PathVariable("userMail") String userMail,
                                  @RequestParam("password") String password,Model model){
        User user = userService.getUserByEmail(userMail);
        user.setPassKeyForMail(password);
        userService.updateUser(user);
        return "redirect:/user/mails/mail-view/"+userMail;
    }

    @GetMapping({"/mail-view/{userMail}"})
    public String mailerview(@PathVariable("userMail") String userMail, Model model) {
        User user = userService.getUserByEmail(userMail);
        Page<Contact> pagecontacts = contactService.getByUser(user,0,AppConstants.PAGE_SIZE,"name","asc");
        model.addAttribute("contacts", pagecontacts);
        model.addAttribute("pageSize", AppConstants.PAGE_SIZE);
        model.addAttribute("contactSearchForm", new ContactSearchForm());
        model.addAttribute("userMail", userMail);
        return "user/mails/mail_front";
    }

    @GetMapping({"/contact-send-mails/{from}/{to}"})
    public String mailTempFront(@PathVariable("from") String from,
                                @PathVariable("to") String to,
                                Model model){
        User user = userService.getUserByEmail(from);
        MailForm mailForm = new MailForm();
        mailForm.setFrom(from);
        mailForm.setTo(to);
        model.addAttribute("mailForm", mailForm);
        return "user/mails/mail_template";
    }

    @PostMapping({"/handler_mail"})
    public String SendTheMail(@ModelAttribute MailForm mailForm, HttpSession session){
        var user = userService.getUserByEmail(mailForm.getFrom());
        String passKey = user.getPassKeyForMail();
        String from = mailForm.getFrom();
        String to = mailForm.getTo();
        String subject = mailForm.getSubject();
        String body = mailForm.getBody();
        emailService.sendEmailToUser(from, to, subject, body,passKey);
        Message message = Message.builder().content("Email Sent successfully...").type(MessageType.green).build();
        session.setAttribute("message", message);
        return "redirect:/user/mails/mail-view/"+from;
    }
}
