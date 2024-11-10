package com.scm.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity(name = "user")
@Table(name = "user_table")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    private String userId;
    @Column(unique = true, nullable = false)
    private String userName;
    private String password;
    @Column(nullable = false, unique = true)
    private String email;
    private String phoneNumber;
    @Column(length = 3000)
    private String about;
    @Column(length = 10000)
    private String profilePic;
    private boolean enabled = false;
    private boolean emailVerified = false;
    private boolean phoneNumberVerified = false;
    @Enumerated(EnumType.STRING)
    private Providers provider;
    private String providerUserId;
    @OneToMany(
            mappedBy = "user",
            cascade = {CascadeType.ALL},
            fetch = FetchType.LAZY,
            orphanRemoval = true
    )
    private List<Contact> contacts;
}
