package com.scm.controllers;

import com.scm.entity.Contact;
import com.scm.entity.User;
import com.scm.forms.ContactForm;
import com.scm.forms.ContactSearchForm;
import com.scm.helper.AppConstants;
import com.scm.helper.Helper;
import com.scm.helper.Message;
import com.scm.helper.MessageType;
import com.scm.services.ContactService;
import com.scm.services.ImageService;
import com.scm.services.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping({"/user/contact"})
public class ContactController {

    @Autowired
    private ContactService contactService;

    @Autowired
    private UserService userService;

    @Autowired
    private ImageService imageService;
    //add conatct page handler
    @RequestMapping(value = {"/add"})
    public String addContactView(Model model) {
        model.addAttribute("contactForm", new Contact());
        return "user/add_contact";
    }

    @PostMapping({"/process-contact"})
    public String addContact(@Valid @ModelAttribute ContactForm contactForm, BindingResult bindingResult, HttpSession session, Authentication authentication, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("contactForm", contactForm);
            Map<String, String> errorMessages = new HashMap<>();

            // Populate the map with errors if they exist
            bindingResult.getFieldErrors().forEach(error -> {
                errorMessages.put(error.getField(), error.getDefaultMessage());
            });


            // Ensure errorMessages is always added to the model
            model.addAttribute("errorMessages", errorMessages);
            Message message = Message.builder().content("Please Fill the Correct Details").type(MessageType.red).build();
            session.setAttribute("message", message);
            return "user/add_contact";
        }
        else{
            String userEmail = Helper.getEmailOfLoggedInUser(authentication);
            User user = userService.getUserByEmail(userEmail);
            System.out.println(contactForm);
            System.out.println(contactForm.getPicture().getOriginalFilename());
            Contact contact = new Contact();
            contact.setName(contactForm.getName());
            contact.setEmail(contactForm.getEmail());
            contact.setAddress(contactForm.getAddress());
            contact.setDescription(contactForm.getDescription());
            contact.setFavorite(contactForm.isFavorite());
            contact.setInstagramLink(contactForm.getInstagramLink());
            contact.setLinkedinLink(contactForm.getLinkedinLink());
            contact.setPhoneNumber(contactForm.getPhoneNumber());
            contact.setUser(user);

            //image upload code
            if(contactForm.getPicture() != null && !contactForm.getPicture().isEmpty()) {
                String filename = UUID.randomUUID().toString();
                String fileURL = imageService.uploadImage(contactForm.getPicture(),filename);
                contact.setPicture(fileURL);
                contact.setCloudinaryImagePublicId(filename);
            }

            contactService.save(contact);
            Message message = Message.builder().content("Contact Added Successfully").type(MessageType.green).build();
            session.setAttribute("message", message);
            return "redirect:/user/contact/add";
        }
    }

    @RequestMapping
    public String viewContact(
            @RequestParam(value="page",defaultValue = "0") int page,
            @RequestParam(value="size",defaultValue = "10") int size,
            @RequestParam(value ="sortBy",defaultValue = "name") String sortBy,
            @RequestParam(value="direction",defaultValue = "asc") String direction,
            Authentication authentication, Model model) {
        String userEmail = Helper.getEmailOfLoggedInUser(authentication);
        User user = userService.getUserByEmail(userEmail);
        Page<Contact> pagecontacts = contactService.getByUser(user,page,size,sortBy,direction);
        model.addAttribute("contacts", pagecontacts);
        model.addAttribute("pageSize", AppConstants.PAGE_SIZE);
        model.addAttribute("contactSearchForm", new ContactSearchForm());
        return "user/view_contact";
    }

    //search handler
    @RequestMapping({"/search"})
    public String searchHandler(
            @ModelAttribute ContactSearchForm contactSearchForm,
            @RequestParam(value="page",defaultValue = "0") int page,
            @RequestParam(value="size",defaultValue = AppConstants.PAGE_SIZE+"") int size,
            @RequestParam(value ="sortBy",defaultValue = "name") String sortBy,
            @RequestParam(value="direction",defaultValue = "asc") String direction,
            Authentication authentication,
            Model model
    ){
        String field = contactSearchForm.getField();
        String value = contactSearchForm.getValue();
        System.out.println("field: "+field+" and keyword: "+value);
        var user = userService.getUserByEmail(Helper.getEmailOfLoggedInUser(authentication));
        Page<Contact> pageContact = null;
        if(field.equalsIgnoreCase("name")){
            pageContact = contactService.searchByName(value,size,page,sortBy,direction,user);
        }
        else if(field.equalsIgnoreCase("phone")){
            pageContact = contactService.searchByPhoneNumber(value,size,page,sortBy,direction,user);
        } else if(field.equalsIgnoreCase("email")){
            pageContact = contactService.searchByEmail(value,size,page,sortBy,direction,user);
        }
        System.out.println(pageContact);
        model.addAttribute("contacts",pageContact);
        model.addAttribute("emptyContact",pageContact.isEmpty());
        model.addAttribute("contactSearchForm",contactSearchForm);
        model.addAttribute("pageSize", AppConstants.PAGE_SIZE);
        return "user/search";
    }

    //delete handler
    @RequestMapping({"/delete/{contactId}"})
    public String deleteContact(@PathVariable("contactId") String contactId, Authentication authentication, Model model) {
        contactService.delete(contactId);
        System.out.println("Contact deleted successfully");
        return "redirect:/user/contact?size="+AppConstants.PAGE_SIZE+"&page=0";
    }

    //update contact view
    @GetMapping({"/view/{contactId}"})
    public String updateContactFormView(@PathVariable("contactId") String contactId,
                                Model model) {
        var contact1 = contactService.getById(contactId);
        ContactForm contactForm = new ContactForm();
        contactForm.setName(contact1.getName());
        contactForm.setEmail(contact1.getEmail());
        contactForm.setAddress(contact1.getAddress());
        contactForm.setDescription(contact1.getDescription());
        contactForm.setFavorite(contact1.isFavorite());
        contactForm.setInstagramLink(contact1.getInstagramLink());
        contactForm.setLinkedinLink(contact1.getLinkedinLink());
        contactForm.setPhoneNumber(contact1.getPhoneNumber());
        contactForm.setContactPicture(contact1.getPicture());
        model.addAttribute("contactForm", contactForm);
        model.addAttribute("contactId",contactId);
        return "user/update_contact_view";
    }

    @PostMapping({"/update/{contactId}"})
    public String updateContact(@PathVariable("contactId") String contactId,
                                @Valid @ModelAttribute ContactForm contactForm,
                                BindingResult bindingResult,
                                HttpSession session,Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("contactForm", contactForm);
            Map<String, String> errorMessages = new HashMap<>();

            // Populate the map with errors if they exist
            bindingResult.getFieldErrors().forEach(error -> {
                errorMessages.put(error.getField(), error.getDefaultMessage());
            });


            // Ensure errorMessages is always added to the model
            model.addAttribute("errorMessages", errorMessages);
            Message message = Message.builder().content("Please Fill the Correct Details").type(MessageType.red).build();
            session.setAttribute("message", message);
            return "user/update_contact_view";
        }
        else{
            var contact2 = contactService.getById(contactId);
            contact2.setId(contactId);
            contact2.setName(contactForm.getName());
            contact2.setEmail(contactForm.getEmail());
            contact2.setAddress(contactForm.getAddress());
            contact2.setDescription(contactForm.getDescription());
            contact2.setFavorite(contactForm.isFavorite());
            contact2.setInstagramLink(contactForm.getInstagramLink());
            contact2.setLinkedinLink(contactForm.getLinkedinLink());
            contact2.setPhoneNumber(contactForm.getPhoneNumber());

            if(contactForm.getPicture() != null && !contactForm.getPicture().isEmpty()) {
                System.out.println("File is not empty");
                String fileName = UUID.randomUUID().toString();
                String imageUrl = imageService.uploadImage(contactForm.getPicture(),fileName);
                contact2.setCloudinaryImagePublicId(fileName);
                contact2.setPicture(imageUrl);
                contactForm.setContactPicture(imageUrl);
            }
            else{
                System.out.println("File is empty");
            }
            contactService.update(contact2);
            Message message = Message.builder().content("Contact Updated Successfully").type(MessageType.green).build();
            session.setAttribute("message", message);
            return "redirect:/user/contact?size="+AppConstants.PAGE_SIZE+"&page=0";
        }
    }

}
