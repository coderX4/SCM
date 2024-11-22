package com.scm.services;

import com.scm.entity.Contact;
import com.scm.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ContactService {
    Contact save(Contact contact);
    Contact update(Contact contact);
    void delete(String id);
    List<Contact> getAll();
    Contact getById(String id);
    List<Contact> getByUserId(String userId);
    Page<Contact> getByUser(User user,int page,int size,String sortField,String sortDirection);
    Page<Contact> searchByName(String nameKeyword,int size,int page,String sortBy,String orderBy);
    Page<Contact> searchByPhoneNumber(String phoneNumberKeyword,int size,int page,String sortBy,String orderBy);
    Page<Contact> searchByEmail(String emailKeyword,int size,int page,String sortBy,String orderBy);

}
