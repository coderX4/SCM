package com.scm.controllers;

import com.scm.entity.User;
import com.scm.helper.Helper;
import com.scm.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class RootController {
    @Autowired
    UserService userService;

    Logger logger = LoggerFactory.getLogger(RootController.class);
    @ModelAttribute
    public void addLoggedInUserInformation(Authentication authentication, Model model){
        if(authentication == null){
            return;
        }
        System.out.println("Added Logged In User Information");
        String user1 = Helper.getEmailOfLoggedInUser(authentication);
        logger.info("User: {}", user1);
        User user = userService.getUserByEmail(user1);

        if(user == null){
            model.addAttribute("error", "User not found");
        }
        else{
            String picurl = user.getProfilePic();
            System.out.println("pic url : "+picurl);
            model.addAttribute("picurl", picurl);
            model.addAttribute("userName", user.getUName());
            model.addAttribute("loggedInUser", user);
        }
    }
}
