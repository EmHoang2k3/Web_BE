package com.project.shopapp.models;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Table(name = "tokens")
@Entity
@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "token",length = 255)
    private String token;

    @Column(name = "token_type",length = 50)
    private String tokenType;


    @Column(name ="expiration_date")
    private LocalDateTime expirationDate;

    private boolean revoked;
    private boolean expired;

    @ManyToOne
    @Column(name = "user_id")
    private UserModel user;
}
