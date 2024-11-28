package com.scm.services;

import com.scm.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User saveUser(User user);

    User getUserById(String id);

    User updateUser(User user);

    void deleteUser(String id);

    boolean isUserExist(String userId);

    boolean isUserExistByEmail(String email);

    List<User> getAllUsers();

    User getUserByEmail(String email);
}

