package com.scm.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity(name = "sociallink")
@Table(name = "social_table")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SocialLink {
    @Id
    private Long id;
    private String link;
    private String title;
    @ManyToOne
    private Contact contact;
}
