package com.scm.helper;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
public class SessionHelper {
    public SessionHelper() {
    }

    public static void removeMessage() {
        try {
            HttpSession session = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession();
            session.removeAttribute("message");
        } catch (Exception var1) {
            Exception e = var1;
            System.out.println("Error in removeMessage: " + String.valueOf(e));
            e.printStackTrace();
        }

    }
}
