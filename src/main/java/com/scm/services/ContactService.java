package com.scm.services;

import com.scm.entity.Contact;
import com.scm.entity.User;

import java.util.List;

public interface ContactService {
    Contact save(Contact contact);
    Contact update(Contact contact);
    void delete(String id);
    List<Contact> getAll();
    Contact getById(String id);
    List<Contact> search(String name, String email,String phoneNumber);
    List<Contact> getByUserId(String userId);
    List<Contact> getByUser(User user);
}
