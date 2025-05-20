package com.scm.helper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class Helper {
    @Value("${scm.baseurl}")
    static String baseUrl;

    public static String getEmailOfLoggedInUser(Authentication authentication) {

        //if sign in with Google and GitHub
        if(authentication instanceof OAuth2AuthenticationToken) {
            var aOAuth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;
            var clientId = aOAuth2AuthenticationToken.getAuthorizedClientRegistrationId();

            var oauth2User = (OAuth2User)authentication.getPrincipal();
            String userName = "";
            //if sign in GOOGLE
            if(clientId.equalsIgnoreCase("GOOGLE")) {
                System.out.println("GOOGLE Account Logged in");
                userName = (String) oauth2User.getAttribute("email");

            }
            //if sign in GITHUB
            else if (clientId.equalsIgnoreCase("GITHUB")) {
                System.out.println("GITHUB Account Logged in");
                userName = oauth2User.getAttribute("email") != null ? oauth2User.getAttribute("email").toString()
                        : oauth2User.getAttribute("login").toString() + "@gmail.com";
            }
            return userName;

        }
        //if sign in with SELF
        else{
            System.out.println("User from local database Logged in");
            return authentication.getName();
        }
    }

    public static String getLinkForEmailVerification(String emailToken) {
        return baseUrl +"/auth/verify-email?token=" + emailToken;
    }
}
