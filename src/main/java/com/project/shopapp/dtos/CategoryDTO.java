package com.project.shopapp.dtos;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryDTO {
    @NotEmpty(message = "Category name can not be empty")
    private String name;

    @Column(name = "image_thumnail")
    private String imageThumbnail;
}
