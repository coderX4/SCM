package com.scm.controllers;

import com.scm.entity.User;
import com.scm.helper.Message;
import com.scm.helper.MessageType;
import com.scm.repositories.UserRepo;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping({"/auth"})
public class AuthController {

    private UserRepo userRepo;

    public AuthController(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @GetMapping({"/verify-email"})
    public String verifyEmail(@RequestParam("token") String token,
                              Model model, HttpSession session) {
        System.out.println("Verify email");
        User user = userRepo.findByEmailToken(token).orElse(null);
        if(user != null) {
            if(user.getEmailToken().equals(token)) {
                user.setEnabled(true);
                user.setEmailVerified(true);
                userRepo.save(user);
                Message message = Message.builder().content("Verification Successful").type(MessageType.green).build();
                session.setAttribute("message", message);
                return "redirect:/login?verification=success";
            }
            else{
                Message message = Message.builder().content("Verification Failed").type(MessageType.red).build();
                session.setAttribute("message", message);
                return "redirect:/login?verification=failure";
            }
        }
        Message message = Message.builder().content("Account Not Created ... Some error occurred..").type(MessageType.red).build();
        session.setAttribute("message", message);
        return "redirect:/signup?verification=failure";
    }
}
