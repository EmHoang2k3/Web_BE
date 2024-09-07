package com.project.shopapp.service;
import com.project.shopapp.dtos.CategoryDTO;
import com.project.shopapp.models.CategoryModel;
import com.project.shopapp.responses.CategoryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface ICategoryService {

    CategoryModel createCategory (CategoryDTO category);

    CategoryModel getCategoryById (long id);

//    List<CategoryModel> getAllCategory();

    //    @Override
    //    public List<CategoryModel> getAllCategory() {
    //        return null;
    //    }
    Page<CategoryResponse> getAllCategory(PageRequest pageRequest);

    CategoryModel updateCategory(long id, CategoryDTO category);

    void remoteCategory(long id);



}
