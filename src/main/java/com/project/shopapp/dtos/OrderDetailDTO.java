package com.project.shopapp.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import lombok.*;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailDTO {
    @JsonProperty("order_id")
    @Min(value = 1, message = "Order id must be > 0")
    private Long orderId;

    @JsonProperty("product_id")
    @Min(value = 1, message = "product id must be > 0")
    private Long productId;

    @Min(value = 1, message = "Price id must be > 0")
    private Long price;

    @JsonProperty("number_of_product")
    @Min(value = 1, message = "number_of_product id must be >= 1")
    private int numberOfProduct;

    @JsonProperty("total_money")
    @Min(value = 1, message = "total_money id must be > 0")
    private Float totalMoney;

    private String color;
}
