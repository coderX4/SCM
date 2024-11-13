package com.scm.services.impl;

import com.scm.entity.User;
import com.scm.helper.AppConstants;
import com.scm.helper.ResourceNotFoundException;
import com.scm.repositories.UserRepo;
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
        return (User)this.userRepo.save(user);
    }

    public Optional<User> getUserById(String id) {
        return this.userRepo.findById(id);
    }

    public Optional<User> updateUser(User user) {
        User user2 = (User)this.userRepo.findById(user.getUserId()).orElseThrow(() -> {
            return new ResourceNotFoundException("User Not Found");
        });
        user2.setUserName(user.getUsername());
        user2.setPassword(user.getPassword());
        user2.setEmail(user.getEmail());
        user2.setAbout(user.getAbout());
        user2.setPhoneNumber(user.getPhoneNumber());
        user2.setProfilePic(user.getProfilePic());
        user2.setEnabled(user.isEnabled());
        user2.setEmailVerified(user.isEmailVerified());
        user2.setPhoneNumberVerified(user.isPhoneNumberVerified());
        user2.setProvider(user.getProvider());
        user2.setProviderUserId(user.getProviderUserId());
        User savedUser = (User)this.userRepo.save(user2);
        return Optional.ofNullable(savedUser);
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

