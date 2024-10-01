package com.project.shopapp.service;
import com.project.shopapp.dtos.CategoryDTO;
import com.project.shopapp.exceptions.DataNotFoundException;
import com.project.shopapp.models.CategoryModel;
import com.project.shopapp.responses.CategoryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface ICategoryService {

    CategoryModel createCategory (CategoryDTO category);

    CategoryModel getCategoryById (long id) throws DataNotFoundException;

    Page<CategoryResponse> getAllCategory(PageRequest pageRequest);

    CategoryModel updateCategory(long id, CategoryDTO category) throws DataNotFoundException;

    void remoteCategory(long id);



}
