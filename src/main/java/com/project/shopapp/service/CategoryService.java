package com.project.shopapp.service;

import com.project.shopapp.dtos.CategoryDTO;
import com.project.shopapp.exceptions.DataNotFoundException;
import com.project.shopapp.models.CategoryModel;
import com.project.shopapp.repositories.CategoryRepository;
import com.project.shopapp.responses.CategoryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
                .imageThumbnail(categoryDTO.getImageThumbnail()) // Lưu URL của ảnh vào trường imageThumbnail
                .build();
        return categoryRepository.save(newCategory);
    }

    @Override
    public CategoryModel getCategoryById(long id) {
        return categoryRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Category not found"));
    }


    @Override
    public Page getAllCategory(PageRequest pageRequest) {
        Page<CategoryModel> categoryPage = categoryRepository.findAll(pageRequest);
        return categoryPage.map(CategoryResponse :: formCategory);
    }

    @Override
    public CategoryModel updateCategory(long id, CategoryDTO categoryDTO) {
        // Lấy category hiện có từ database
        CategoryModel existingCategory = getCategoryById(id);

        // Cập nhật tên category
        existingCategory.setName(categoryDTO.getName());

        // Kiểm tra xem có hình ảnh mới hay không
        if (categoryDTO.getImageThumbnail() != null && !categoryDTO.getImageThumbnail().isEmpty()) {
            // Nếu có hình ảnh, cập nhật đường dẫn ảnh
            existingCategory.setImageThumbnail(categoryDTO.getImageThumbnail());
        }

        // Lưu cập nhật vào database
        categoryRepository.save(existingCategory);

        return existingCategory;
    }

    @Override
    public void remoteCategory(long id) {
        //Xóa cứng
        categoryRepository.deleteById(id);
    }


    public CategoryModel updateCategoryImage(Long categoryId, String imageUrl) throws Exception {
        CategoryModel existingCategory = categoryRepository
                .findById(categoryId)
                .orElseThrow(() -> new DataNotFoundException("Category not found"));

        existingCategory.setImageThumbnail(imageUrl); // Cập nhật URL của ảnh
        return categoryRepository.save(existingCategory);
    }

}
