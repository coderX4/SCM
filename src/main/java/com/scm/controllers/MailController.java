package com.scm.controllers;

import com.scm.entity.User;
import com.scm.services.EmailService;
import com.scm.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping({"/user/mails"})
public class MailController {
    @Autowired
    private EmailService emailService;

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

        String apppassword = user.getPassKeyForMail();
        String to = "nietclllg@gmail.com";
        String subject = "Users MAilingn";
        String body = "Maiking to a friend";
        emailService.sendEmailToUser(userMail, to, subject, body, apppassword);
        return "redirect:/";
    }
}
