package com.scm.repositories;

import com.scm.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User, String> {

    User findByUserId(String userId);
    Optional<User> findByEmail(String email);
    Optional<User> findByEmailToken(String token);
}
