package com.project.shopapp.controllers;

import com.project.shopapp.dtos.CategoryDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/categories")
//@Validated
public class CategoryController {
    //Hiển thị tất cả category
    @GetMapping
    //http://localhost:8088/api/v1/categories?page=1&limit=10
    public ResponseEntity<String> GetAllCategories(
            @RequestParam("page")    int page,
            @RequestParam("limit")   int limit
    ){
        return ResponseEntity.ok("thành công");
    }

    @PostMapping
    public ResponseEntity<?> insertCategory(@Valid @RequestBody
                                                CategoryDTO categoryDTO,
                                                BindingResult result){
        if(result.hasErrors()){
            List<String> errorMessage = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            return ResponseEntity.badRequest().body(errorMessage);
        }
        return ResponseEntity.ok("thành công");
    }
}
