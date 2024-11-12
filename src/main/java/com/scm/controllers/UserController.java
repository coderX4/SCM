package com.scm.controllers;

import com.scm.config.OAuthAuthenticationSuccessHandler;
import com.scm.entity.User;
import com.scm.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping({"/user"})
public class UserController {

    @Autowired
    private UserService userService;

    //user dashboard
    @RequestMapping(value = {"/dashboard"}, method = RequestMethod.GET)
    public String userDashboard() {
        return "user/dashboard";
    }

    //user profile page
    @RequestMapping(value = {"/profile"}, method = RequestMethod.GET)
    public String userProfile() {
        return "user/profile";
    }
    //user add contact page
    //user view contact page
    //user edit conatct page
    //user delete conatct
    //user search contact
}