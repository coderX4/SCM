package com.scm.config;

import com.scm.entity.Providers;
import com.scm.entity.User;
import com.scm.helper.AppConstants;
import com.scm.repositories.UserRepo;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
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

    private UserRepo userRepo;

    @Autowired
    public OAuthAuthenticationSuccessHandler(ApplicationContext context) {
        this.userRepo = context.getBean(UserRepo.class);
    }

    Logger logger = LoggerFactory.getLogger(OAuthAuthenticationSuccessHandler.class);

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        logger.info("Authentication success");

        DefaultOAuth2User oAuth2User = (DefaultOAuth2User)authentication.getPrincipal();

//        logger.info("oAuth2Useruser: {}", oAuth2Useruser.getName());
//        oAuth2Useruser.getAttributes().forEach((key, value) -> {
//            logger.info("key: {}, value: {}", key, value);
//        });
//        logger.info(oAuth2Useruser.getAuthorities().toString());

        String Email = oAuth2User.getAttribute("email");
        logger.info("Email: " + Email);
        String userName = oAuth2User.getAttribute("name");
        logger.info("userName: " + userName);
        String picture = oAuth2User.getAttribute("picture");
        logger.info("picture: " + picture);

        //create user and save in database
        User user = new User();
        user.setEmail(Email);
        user.setUserName(userName);
        user.setProfilePic(picture);
        user.setPassword("SCM@123");
        user.setUserId(UUID.randomUUID().toString());
        user.setProvider(Providers.GOOGLE);
        user.setEnabled(true);
        user.setEmailVerified(true);
        user.setProviderUserId(oAuth2User.getName());
        user.setRolesList(List.of(AppConstants.ROLE_USER));
        user.setAbout("This Account is created by google login");


        User user1 = userRepo.findByEmail(Email).orElse(null);
        if (user1 == null) {
            userRepo.save(user);
            logger.info("User created" + Email);
        }
        new DefaultRedirectStrategy().sendRedirect(request, response, "/user/dashboard");

    }

}
