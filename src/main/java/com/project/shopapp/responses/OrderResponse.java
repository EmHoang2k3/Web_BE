package com.project.shopapp.responses;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.shopapp.models.OrderDetailModel;
import com.project.shopapp.models.OrderModel;

import com.project.shopapp.models.UserModel;
import lombok.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderResponse {
    private Long id;

    @JsonProperty("user_id")
    private UserModel user;

    @JsonProperty("fullname")
    private String fullName;

    @JsonProperty("phone_number")
    private String phoneNumber;

    @JsonProperty("email")
    private String email;

    @JsonProperty("address")
    private String address;

    @JsonProperty("note")
    private String note;

    @JsonProperty("order_date")
    private LocalDate orderDate;

    @JsonProperty("status")
    private String status;

    @JsonProperty("total_money")
    private Float totalMoney;

    @JsonProperty("shipping_method")
    private String shippingMethod;

    @JsonProperty("shipping_address")
    private String shippingAddress;

    @JsonProperty("shipping_date")
    private LocalDate shippingDate;

    @JsonProperty("tracking_number")
    private String trackingNumber;

    @JsonProperty("payment_method")
    private String paymentMethod;

   @JsonProperty("order_detail")
    private List<OrderDetailModel> orderDetail;


   public static OrderResponse formOrder(OrderModel order){
       OrderResponse orderResponse = OrderResponse.builder()
               .id(order.getId())
               .user(order.getUser())
               .fullName(order.getFullName())
               .phoneNumber(order.getPhoneNumber())
               .email(order.getEmail())
               .address(order.getAddress())
               .note(order.getNote())
               .orderDate(order.getOrderDate().toInstant()
                       .atZone(ZoneId.systemDefault())
                       .toLocalDate())
               .status(order.getStatus())
               .totalMoney(order.getTotalMoney())
               .paymentMethod(order.getPaymentMethod())
               .shippingAddress(order.getShippingAddress())
               .shippingDate(order.getShippingDate())
               .trackingNumber(order.getTrackingNumber())
               .paymentMethod(order.getPaymentMethod())
               .orderDetail(order.getOrderDetail())
               .build();

       return orderResponse;
   }
}
