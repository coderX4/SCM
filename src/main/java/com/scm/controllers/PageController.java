package com.scm.controllers;

import com.scm.entity.User;
import com.scm.forms.UserSignupForm;
import com.scm.helper.Message;
import com.scm.helper.MessageType;
import com.scm.services.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.HashMap;
import java.util.Map;

@Controller
public class PageController {
    @Autowired
    private UserService userService;

    public PageController() {
    }

    @RequestMapping({"/home"})
    public String home(Model model) {
        System.out.println("home page handler");
        model.addAttribute("name", "Dogggg,,,");
        return "home";
    }

    @RequestMapping({"/about"})
    public String about() {
        return "about";
    }

    @RequestMapping({"/services"})
    public String services() {
        return "service";
    }

    @RequestMapping({"/contact"})
    public String contact() {
        return "contact";
    }

    @RequestMapping({"/login"})
    public String login() {
        return "login";
    }

    @RequestMapping(value = {"/signup"},method = RequestMethod.GET)
    public String signup(Model model) {
        UserSignupForm userForm = new UserSignupForm();
        model.addAttribute("userForm", userForm);
        return "signup";
    }

    @RequestMapping(value = {"/do-register"}, method = RequestMethod.POST)
    public String processSignup(@Valid @ModelAttribute UserSignupForm userForm, BindingResult bindingResult, HttpSession session,Model model) {
        System.out.println(userForm);
        if (bindingResult.hasErrors()) {
            model.addAttribute("userForm", userForm);
            Map<String, String> errorMessages = new HashMap<>();

            // Populate the map with errors if they exist
            bindingResult.getFieldErrors().forEach(error -> {
                errorMessages.put(error.getField(), error.getDefaultMessage());
                });


            // Ensure errorMessages is always added to the model
            model.addAttribute("errorMessages", errorMessages);
            return "signup";

        } else {
            User user = new User();
            user.setUserName(userForm.getUserName());
            user.setPassword(userForm.getPassword());
            user.setEmail(userForm.getEmail());
            user.setPhoneNumber(userForm.getPhoneNumber());
            user.setAbout(userForm.getAbout());
            user.setProfilePic("fequfufb");
            User savedUser = this.userService.saveUser(user);
            System.out.println("user saved: " + savedUser);
            Message message = Message.builder().content("Registration successful").type(MessageType.blue).build();
            session.setAttribute("message", message);
            return "redirect:/signup";
        }
    }
}
