package com.project.shopapp.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductImageDTO {
//    @JsonProperty("product_id")
//    @Min(value = 1,message = "Product's ID must be > 0")
//    private Long productId;
    private Long id;

    @Size(min = 5,max = 200,message = "Image's Name")
    @JsonProperty("image_url")
    private String imageUrl;
}
