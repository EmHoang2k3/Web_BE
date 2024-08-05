package com.project.shopapp.responses;

import com.project.shopapp.models.OrderModel;
import com.project.shopapp.models.ProductModel;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDetailResponse {

    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Long order;

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
