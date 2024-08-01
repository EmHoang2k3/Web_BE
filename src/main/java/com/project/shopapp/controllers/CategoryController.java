package com.project.shopapp.controllers;

import com.project.shopapp.dtos.CategoryDTO;
import com.project.shopapp.models.CategoryModel;
import com.project.shopapp.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/categories")
@RequiredArgsConstructor
//@Validated
public class CategoryController {

    private final CategoryService categoryService;


    @PostMapping
    public ResponseEntity<?> createCategory(@Valid @RequestBody
                                            CategoryDTO categoryDTO,
                                            BindingResult result){
        if(result.hasErrors()){
            List<String> errorMessage = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            return ResponseEntity.badRequest().body(errorMessage);
        }
        categoryService.createCategory(categoryDTO);
        return ResponseEntity.ok("Create category successfully");
    }

    //Hiển thị tất cả category
    @GetMapping
    //http://localhost:8088/api/v1/categories?page=1&limit=10
    public ResponseEntity<List<CategoryModel>> GetAllCategories(
            @RequestParam("page")    int page,
            @RequestParam("limit")   int limit
    ){
      List<CategoryModel> categories = categoryService.getAllCategory();
        return ResponseEntity.ok(categories);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateCategory(@PathVariable Long id,
                                                 @RequestBody
                                                 @Valid
                                                 CategoryDTO categoryDTO){
        categoryService.updateCategory(id,categoryDTO);
        return ResponseEntity.ok("Update category successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long id){
        categoryService.remoteCategory(id);
        return ResponseEntity.status(HttpStatus.OK).body("Delete category successfully");
    }
}
