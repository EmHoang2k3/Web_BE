package com.project.shopapp.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.shopapp.models.CategoryModel;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDTO {
    private Long id;

    @NotBlank(message = "Title is required")
    @Size(min = 3,max = 200,message = "Title must be between 3 and 200 characters")
    private String name;

    @Min(value = 0,message = "Price must be greater than or equal to 0")
    @Max(value = 10000000,message = "Price must be less than or equal to 10,000,000")
    private Float price;
    private String thumbnail;
    private String description;

    @JsonProperty("category_id")
    private Long categoryId;
    private List<ProductImageDTO> product_images;
    private boolean active;
//    private List<MultipartFile> files;

}
