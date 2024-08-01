package com.project.shopapp.service;

import com.project.shopapp.dtos.CategoryDTO;
import com.project.shopapp.models.CategoryModel;
import com.project.shopapp.repositories.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService{

    private final CategoryRepository categoryRepository;

    @Override
    public CategoryModel createCategory(CategoryDTO categoryDTO) {
        CategoryModel newCategory = CategoryModel.builder()
                .name(categoryDTO.getName())
                .build();
        return categoryRepository.save(newCategory);
    }

    @Override
    public CategoryModel getCategoryById(long id) {
        return categoryRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Category not found"));
    }

    @Override
    public List<CategoryModel> getAllCategory() {
        return categoryRepository.findAll();
    }

    @Override
    public CategoryModel updateCategory(long id, CategoryDTO categoryDTO) {
        CategoryModel existingCategory = getCategoryById(id);
        existingCategory.setName(categoryDTO.getName());
        categoryRepository.save(existingCategory);
        return existingCategory;
    }

    @Override
    public void remoteCategory(long id) {
        //Xóa cứng
        categoryRepository.deleteById(id);
    }
}
