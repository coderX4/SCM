package com.scm.repositories;

import com.scm.entity.Contact;
import com.scm.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContactRepo extends JpaRepository<Contact,String> {
    List<Contact> findByUser(User user);
    @Query("SELECT c from contact  c where c.user.userId = :userId")
    List<Contact> findByUserId(String userId);
}
