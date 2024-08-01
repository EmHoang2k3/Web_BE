package com.project.shopapp.models;

import jakarta.persistence.*;
import lombok.*;

@Table(name = "social_account")
@Entity
@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SocialAccountModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "provider",length = 20)
    private String provider;

    @Column(name = "provider_id",length = 50)
    private String providerId;

    @Column(name = "email",length = 150)
    private String email;

    @Column(name = "name",length = 100)
    private String name;

    @ManyToOne
    @Column(name = "user_id")
    private UserModel user;

}
