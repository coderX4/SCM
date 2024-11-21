package com.scm.controllers;

import com.scm.entity.Contact;
import com.scm.entity.User;
import com.scm.forms.ContactForm;
import com.scm.helper.Helper;
import com.scm.helper.Message;
import com.scm.helper.MessageType;
import com.scm.services.ContactService;
import com.scm.services.ImageService;
import com.scm.services.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
            String filename = UUID.randomUUID().toString();
            String fileURL = imageService.uploadImage(contactForm.getPicture(),filename);
            contact.setPicture(fileURL);
            contact.setCloudinaryImagePublicId(filename);

            contactService.save(contact);
            Message message = Message.builder().content("Contact Added Successfully").type(MessageType.green).build();
            session.setAttribute("message", message);
            return "redirect:/user/contact/add";
        }
    }

    @GetMapping(value = {"/view-contact"})
    public String viewContact(Authentication authentication, Model model) {
        String userEmail = Helper.getEmailOfLoggedInUser(authentication);
        User user = userService.getUserByEmail(userEmail);
        List<Contact> contacts = contactService.getByUser(user);
        model.addAttribute("contacts", contacts);
        return "user/view_contact";
    }
}
