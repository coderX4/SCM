package com.scm.config;

import com.scm.helper.Message;
import com.scm.helper.MessageType;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class AuthenticationFailure implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        if(exception instanceof DisabledException){
            //user is disabled
            HttpSession session = request.getSession();
            Message message = Message.builder().content("User is Disabled, Email is sent for verification on your email id..").type(MessageType.red).build();
            session.setAttribute("message", message);
            response.sendRedirect("/login");
        }
        else{
            HttpSession session = request.getSession();
            Message message = Message.builder().content("Incorrect Details").type(MessageType.red).build();
            session.setAttribute("message", message);
            response.sendRedirect("/login?error=true");
        }
    }
}
