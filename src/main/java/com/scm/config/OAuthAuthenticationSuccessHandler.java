package com.scm.config;

import com.scm.entity.Providers;
import com.scm.entity.User;
import com.scm.helper.AppConstants;
import com.scm.repositories.UserRepo;
import com.scm.services.impl.UserServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Component
public class OAuthAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private User user;

    Logger logger = LoggerFactory.getLogger(OAuthAuthenticationSuccessHandler.class);

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        logger.info("Authentication success");

        DefaultOAuth2User oAuth2Useruser = (DefaultOAuth2User)authentication.getPrincipal();

//        logger.info("oAuth2Useruser: {}", oAuth2Useruser.getName());
//        oAuth2Useruser.getAttributes().forEach((key, value) -> {
//            logger.info("key: {}, value: {}", key, value);
//        });
//        logger.info(oAuth2Useruser.getAuthorities().toString());

        String Email = oAuth2Useruser.getAttribute("email");
        //logger.info("Email: " + Email);
        String userName = oAuth2Useruser.getAttribute("name");
        //logger.info("userName: " + userName);
        String picture = oAuth2Useruser.getAttribute("picture");
        //logger.info("picture: " + picture);

        //create user and save in database
//        user.setEmail(Email);
//        user.setUserName(userName);
//        user.setProfilePic(picture);
//        user.setPassword("SCM@123");
//        user.setUserId(UUID.randomUUID().toString());
//        user.setProvider(Providers.GOOGLE);
//        user.setEnabled(true);
//        user.setEmailVerified(true);
//        user.setProviderUserId(oAuth2Useruser.getName());
//        user.setRolesList(List.of(AppConstants.ROLE_USER));
//        user.setAbout("This Account is created by google login");
//
//        sendUser(Email);
//        User user1 = userRepo.findByEmail(Email).orElse(null);
//        if (user1 == null) {
//            userRepo.save(user);
//            logger.info("User created" + Email);
//        }
        new DefaultRedirectStrategy().sendRedirect(request, response, "/user/dashboard");

        user = setGoogleUser(Email, userName, picture);

        logger.info("email: " + user.getEmail());
        logger.info("name: " + user.getUsername());
        logger.info("picture: " + user.getProfilePic());
        logger.info("Provider: " + user.getProvider());

    }

    public User setGoogleUser(String email, String name, String picture) {

        User user = new User();
        user.setEmail(email);
        user.setUserName(name);
        user.setProfilePic(picture);
        user.setPassword("SCM@123");
        user.setUserId(UUID.randomUUID().toString());
        user.setProvider(Providers.GOOGLE);
        user.setEnabled(true);
        user.setEmailVerified(true);
        user.setRolesList(List.of(AppConstants.ROLE_USER));
        user.setAbout("This Account is created by google login");

        return user;
    }
    

}
