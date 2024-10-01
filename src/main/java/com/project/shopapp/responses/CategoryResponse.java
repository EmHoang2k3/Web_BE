package com.project.shopapp.responses;

import com.project.shopapp.models.CategoryModel;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryResponse{
    private String message;
    private Long id;
    private String name;
    private String thumnail;

    public static CategoryResponse formCategory(CategoryModel category){
        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .thumnail(category.getImageThumbnail())
                .build();


    }
}
