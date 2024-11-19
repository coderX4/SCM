package com.scm.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity(
        name = "contact"
)
@Table(
        name = "contact_table"
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Contact {
    @Id
    private String id;
    private String name;
    private String email;
    private String phoneNumber;
    private String address;
    private String picture;
    @Column(length = 2000)
    private String description;
    private boolean favorite = false;
    private String instagramLink;
    private String linkedinLink;
    @ManyToOne
    private User user;
    @OneToMany(
            mappedBy = "contact",
            cascade = {CascadeType.ALL},
            fetch = FetchType.EAGER,
            orphanRemoval = true
    )
    private List<SocialLink> socialLinks = new ArrayList();

}
