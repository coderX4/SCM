package com.scm.repositories;

import com.scm.entity.Contact;
import com.scm.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContactRepo extends JpaRepository<Contact,String> {
    Page<Contact> findByUser(User user, Pageable pageable);
    @Query("SELECT c from contact  c where c.user.userId = :userId")
    List<Contact> findByUserId(String userId);

    Page<Contact> findByNameContaining(String nameKeyword, Pageable pageable);
    Page<Contact> findByEmailContaining(String emailKeyword, Pageable pageable);
    Page<Contact> findByPhoneNumberContaining(String phoneNumberKeyword, Pageable pageable);
}
