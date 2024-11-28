package com.scm.services.impl;

import com.scm.entity.User;
import com.scm.helper.AppConstants;
import com.scm.helper.Helper;
import com.scm.helper.ResourceNotFoundException;
import com.scm.repositories.UserRepo;
import com.scm.services.EmailService;
import com.scm.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private EmailService emailService;

    private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    public UserServiceImpl() {
    }

    public User saveUser(User user) {
        String userId = UUID.randomUUID().toString();
        user.setUserId(userId);
        //user password encoder
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        //user roles
        user.setRolesList(List.of(AppConstants.ROLE_USER));

        //email token
        String emailToken = UUID.randomUUID().toString();
        user.setEmailToken(emailToken);

        User savedUser = userRepo.save(user);

        //sending verification link to user email
        String emailLink = Helper.getLinkForEmailVerification(emailToken);
        emailService.sendEmail(savedUser.getEmail(),
                "SCM Account Verification Required",
                "Click on this link to verify your email: "+emailLink);
        return savedUser;
    }

    public User getUserById(String id) {
        return this.userRepo.findByUserId(id);
    }

    public User updateUser(User user) {
        User user2 = (User)this.userRepo.findById(user.getUserId()).orElseThrow(() -> {
            return new ResourceNotFoundException("User Not Found");
        });
        user2.setUserName(user.getUName());
        user2.setPassword(user.getPassword());
        user2.setAbout(user.getAbout());
        user2.setPhoneNumber(user.getPhoneNumber());
        user2.setProfilePic(user.getProfilePic());
        user2.setCloudinaryImagePublicId(user.getCloudinaryImagePublicId());
        user2.setPhoneNumberVerified(user.isPhoneNumberVerified());
        User savedUser = (User)this.userRepo.save(user2);
        return savedUser;
    }

    public void deleteUser(String id) {
        User user2 = (User)this.userRepo.findById(id).orElseThrow(() -> {
            return new ResourceNotFoundException("User Not Found");
        });
        this.userRepo.delete(user2);
    }

    public boolean isUserExist(String userId) {
        User user = (User)this.userRepo.findById(userId).orElse(null);
        return user != null ? true : false;
    }

    public boolean isUserExistByEmail(String email) {
        User user = (User)this.userRepo.findByEmail(email).orElse(null);
        return user != null ? true : false;
    }

    public List<User> getAllUsers() {
        return this.userRepo.findAll();
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepo.findByEmail(email).orElse(null);
    }
}

