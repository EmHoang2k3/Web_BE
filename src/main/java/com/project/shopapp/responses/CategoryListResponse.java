package com.project.shopapp.responses;

import lombok.*;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryListResponse {
    private List<CategoryResponse> category;
    private int totalPages;
}
