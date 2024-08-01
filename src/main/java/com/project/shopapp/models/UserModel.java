package com.project.shopapp.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Table(name = "users")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserModel extends BaseModel{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fullname",length = 100)
    private String fullName;

    @Column(name = "phone_number",length = 10,nullable = false)
    private String phoneNumber;

    @Column(name = "address",length = 200)
    private String address;

    @Column(name = "password",length = 200,nullable = false)
    private String password;

    private boolean active;

    @Column(name = "date_of_birth")
    private Date dateOfBirth;

    @Column(name = "facebook_acount_id")
    private int facebookAcountId;

    @Column(name = "google_account_id")
    private int googleAccountId;

    @ManyToOne
    @Column(name = "role_id")
    private RoleModel role;
}
