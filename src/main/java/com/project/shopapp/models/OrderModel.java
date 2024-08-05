package com.project.shopapp.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Table(name = "orders")
@Entity
@Getter
@Data
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserModel user;

    @Column(name = "fullname",length = 100)
    private String fullName;

    @Column(name = "email",length = 100)
    private String email;

    @Column(name = "phone_number",nullable = false,length = 100)
    private String phoneNumber;

    @Column(name = "address",nullable = false,length = 100)
    private String address;

    @Column(name = "note",nullable = false,length = 100)
    private String note;

    @Column(name = "order_date")
    private Date orderDate;

    @Column(name = "status")
    private String status;

    @Column(name = "total_money")
    private Float totalMoney;

    @Column(name = "shipping_method")
    private String shippingMethod;

    @Column(name = "shipping_address")
    private String shippingAddress;

    @Column(name = "shipping_date")
    private LocalDate shippingDate;

    @Column(name = "tracking_number")
    private String trackingNumber;

    @Column(name = "payment_method")
    private String paymentMethod;

    @Column(name = "active")
    private Boolean active; //Thuộc về admin


}
