package com.project.shopapp.service;
import com.project.shopapp.dtos.CategoryDTO;
import com.project.shopapp.models.CategoryModel;

import java.util.List;

public interface ICategoryService {

    CategoryModel createCategory (CategoryDTO category);

    CategoryModel getCategoryById (long id);

    List<CategoryModel> getAllCategory();

    CategoryModel updateCategory(long id,CategoryDTO category);

    void remoteCategory(long id);
}
