package com.scm.controllers;

import com.scm.entity.User;
import com.scm.forms.ContactForm;
import com.scm.forms.UserUpdateForm;
import com.scm.helper.AppConstants;
import com.scm.helper.Helper;
import com.scm.helper.Message;
import com.scm.helper.MessageType;
import com.scm.services.ImageService;
import com.scm.services.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping({"/user"})
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private ImageService imageService;

    Logger logger = LoggerFactory.getLogger(UserController.class);
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

    //delete handler for search page
    @RequestMapping({"/profile/delete/{userId}"})
    public String deleteprofile(
            @PathVariable("userId") String userId,
            Model model, HttpSession session
            ) {
        userService.deleteUser(userId);
        System.out.println("User deleted successfully");
        Message message = Message.builder().content("User Profile Deleted...").type(MessageType.red).build();
        session.setAttribute("message", message);
        return "redirect:/login?user=" + userId +"deletion-success";
    }

    @RequestMapping({"/profile/update-view/{userId}"})
    public String updateprofileview(
            @PathVariable("userId") String userId,
            Model model
    ){
        var user = userService.getUserById(userId);
        UserUpdateForm userForm = new UserUpdateForm();
        userForm.setUName(user.getUName());
        userForm.setPassword(user.getPassword());
        userForm.setPhoneNumber(user.getPhoneNumber());
        userForm.setAbout(user.getAbout());
        userForm.setPicture(user.getProfilePic());
        userForm.setPhoneNumberVerified(user.isPhoneNumberVerified());

        model.addAttribute("userUpdateForm", userForm);
        model.addAttribute("userId",userId);

        System.out.println(user.getUName());
        return "user/user_update_view";
    }

    //update contact view

    @PostMapping({"/profile/update/{userId}"})
    public String updateProfileView(@PathVariable("userId") String userId,
                                @Valid @ModelAttribute UserUpdateForm userUpdateForm,
                                BindingResult bindingResult,
                                HttpSession session,Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("userUpdateForm", userUpdateForm);
            Map<String, String> errorMessages = new HashMap<>();

            // Populate the map with errors if they exist
            bindingResult.getFieldErrors().forEach(error -> {
                errorMessages.put(error.getField(), error.getDefaultMessage());
            });


            // Ensure errorMessages is always added to the model
            model.addAttribute("errorMessages", errorMessages);
            Message message = Message.builder().content("Please Fill the Correct Details").type(MessageType.red).build();
            session.setAttribute("message", message);
            return "user/user_update_view";
        }
        else{
            User userold = userService.getUserById(userId);
            System.out.println(userold.getUName());
            userold.setUserName(userUpdateForm.getUName());
            userold.setPhoneNumber(userUpdateForm.getPhoneNumber());
            userold.setAbout(userUpdateForm.getAbout());
            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            userold.setPassword(passwordEncoder.encode(userUpdateForm.getPassword()));
            userold.setPhoneNumberVerified(userUpdateForm.isPhoneNumberVerified());

            if(userUpdateForm.getProfilePic() != null && !userUpdateForm.getProfilePic().isEmpty()) {
                System.out.println("File is not empty");
                String fileName = UUID.randomUUID().toString();
                String imageUrl = imageService.uploadImage(userUpdateForm.getProfilePic(),fileName);
                userold.setCloudinaryImagePublicId(fileName);
                userold.setProfilePic(imageUrl);
                userUpdateForm.setPicture(imageUrl);
            }
            else{
                System.out.println("File is empty");
            }
            userService.updateUser(userold);
            Message message = Message.builder().content("User Updated Successfully").type(MessageType.green).build();
            session.setAttribute("message", message);
            return "redirect:/user/profile";
        }
    }

}