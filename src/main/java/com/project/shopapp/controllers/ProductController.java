package com.project.shopapp.controllers;

import com.project.shopapp.dtos.ProductDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/products")

public class ProductController {
    @GetMapping("")
    //http://localhost:8088/api/v1/products?page=1&limit=10
    public ResponseEntity<String> GetProducts(
            @RequestParam("page")    int page,
            @RequestParam("limit")   int limit
    ){
        return ResponseEntity.ok("get list product thành công");
    }

    @PostMapping("")
    public ResponseEntity<?> createProduct(
            @Valid @RequestBody ProductDTO productDTO,
                                BindingResult result
    ){
        try{
            if(result.hasErrors()){
                List<String> errorMessage = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessage);
            }
            return ResponseEntity.ok("Product create successfully");
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity<String> getProductById(@PathVariable("id") String productId){
        return ResponseEntity.ok("Product with ID:" + productId);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body("Delete Product successfully");
    }
}
