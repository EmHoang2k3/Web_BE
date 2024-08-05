package com.project.shopapp.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.shopapp.models.OrderDetailModel;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDetailResponse {

    private Long id;

    @JsonProperty("order_id")
    private Long order;

    @JsonProperty("product_id")
    private Long product;

    @JsonProperty("price")
    private Float price;

    @JsonProperty("number_of_product")
    private int numberOfProduct;

    @JsonProperty("total_money")
    private Float totalMoney;

    @JsonProperty("color")
    private String color;


    public static OrderDetailResponse formOrderDetail(OrderDetailModel orderDetail){

        return OrderDetailResponse.builder()
                .id(orderDetail.getId())
                .order(orderDetail.getOrder().getId())
                .product(orderDetail.getProduct().getId())
                .price(orderDetail.getPrice())
                .color(orderDetail.getColor())
                .numberOfProduct(orderDetail.getNumberOfProduct())
                .totalMoney(orderDetail.getTotalMoney())
                .build();
    }
}
