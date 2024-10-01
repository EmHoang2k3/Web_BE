package com.project.shopapp.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Table(name = "order_details")
@Entity
@Getter
@Data
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class OrderDetailModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    @JsonBackReference
    private OrderModel order;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private ProductModel product;

    @Column(name = "price",nullable = false)
    private Float price;

    @Column(name = "number_of_product",nullable = false)
    private int numberOfProduct;

    @Column(name = "total_money",nullable = false)
    private Float totalMoney;

    @Column(name = "color")
    private String color;
}
