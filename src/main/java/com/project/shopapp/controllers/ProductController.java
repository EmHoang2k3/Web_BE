package com.project.shopapp.controllers;

import com.github.javafaker.Faker;
import com.project.shopapp.components.LocalizationUtils;
import com.project.shopapp.dtos.ProductDTO;
import com.project.shopapp.dtos.ProductImageDTO;
import com.project.shopapp.exceptions.DataNotFoundException;
import com.project.shopapp.models.ProductImageModel;
import com.project.shopapp.models.ProductModel;
import com.project.shopapp.responses.ProductListResponse;
import com.project.shopapp.responses.ProductResponse;
import com.project.shopapp.service.ProductService;
import com.project.shopapp.utils.MessageKeys;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("${api.prefix}/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    private final LocalizationUtils localizationUtils;

    @GetMapping("")
    //http://localhost:8088/api/v1/products?page=1&limit=10
    public ResponseEntity<ProductListResponse> GetProducts(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "0",name = "category_id") Long categoryId,
            @RequestParam("page") int page,
            @RequestParam("limit") int limit) {
        PageRequest pageRequest = PageRequest.of(page - 1, limit, Sort.by("id").ascending());

        Page productPage = productService.getAllProduct(keyword,categoryId,pageRequest);
        //Lấy tổng số trang
        int totalPages = productPage.getTotalPages();
        List<ProductResponse> products = productPage.getContent();
        return ResponseEntity.ok(ProductListResponse.builder()
                .product(products)
                .totalPages(totalPages)
                .build());    }

    @GetMapping("/soft-deleted")
    public ResponseEntity<ProductListResponse> getSoftDeletedProducts(
            @RequestParam("page") int page,
            @RequestParam("limit") int limit) {
        PageRequest pageRequest = PageRequest.of(page - 1, limit,Sort.by("id").ascending());
        Page softDeletedProducts = productService.getSoftDeletedProducts(pageRequest);
        int totalPages = softDeletedProducts.getTotalPages();
        List<ProductResponse> products = softDeletedProducts.getContent();
//        return ResponseEntity.ok(softDeletedProducts);
        return ResponseEntity.ok(ProductListResponse.builder()
                .product(products)
                .totalPages(totalPages)
                .build());
    }


    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createProduct(
            @Valid @ModelAttribute ProductDTO productDTO,
            @RequestParam("files") List<MultipartFile> files,
            BindingResult result) {
        try {
            if (result.hasErrors()) {
                List<String> errorMessage = result.getFieldErrors().stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessage);
            }

            // Tạo sản phẩm mới
            ProductModel newProduct = productService.createProduct(productDTO);

            // Xử lý ảnh
          productService.saveProductImage(newProduct,files);

            return ResponseEntity.ok(newProduct);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable long id,
                                           @Valid @ModelAttribute ProductDTO productDTO,
                                           @RequestParam("files") List<MultipartFile> files,
                                           BindingResult result) {
        try {
            if(result.hasErrors()){
                List<String> errorMessage = result.getFieldErrors().stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessage);
            }
            ProductModel productUpdate = productService.updateProducts(id, productDTO);
            productService.saveProductImagesForUpdate(productUpdate,files);
            return ResponseEntity.ok(ProductResponse.formProduct(productUpdate));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
        try {
            productService.deleteProduct(id);
            return ResponseEntity.ok(String.format("Product width delete successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @GetMapping("/category/{categoryId}")
    public ResponseEntity<ProductListResponse> getProductsByCategory(
            @PathVariable Long categoryId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit) {
        PageRequest pageRequest = PageRequest.of(page - 1, limit, Sort.by("id").ascending());
        Page productPage = productService.getProductsByCategory(categoryId, pageRequest);
        int totalPages = productPage.getTotalPages();
        List<ProductResponse> products = productPage.getContent();
        return ResponseEntity.ok(ProductListResponse.builder()
                .product(products)
                .totalPages(totalPages)
                .build());
    }


    @GetMapping("/images/{imageName}")
    public ResponseEntity<?> viewImage(@PathVariable String imageName) throws Exception {
        try {
            java.nio.file.Path imagePath = Paths.get("uploads/" + imageName);
            UrlResource resource = new UrlResource(imagePath.toUri());
            if (resource.exists()) {
                return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(resource);
            }else{
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .body(new UrlResource(Paths.get("uploads/notfound.png").toUri()));
            }
        } catch (Exception e) {
//            throw new InvalidParamException("Errors "+ e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductDetail(@PathVariable Long id) throws Exception {
        ProductDTO productDetail = productService.getProductDetail(id);
        return ResponseEntity.ok(productDetail);
    }

    @GetMapping("/by-ids")
    public ResponseEntity<?> getProductsByIds(@RequestParam("ids") String ids){
        try {
            List<Long> productIds = Arrays.stream(ids.split(","))
                    .map(Long :: parseLong)
                    .collect(Collectors.toList());
            List<ProductModel> product = productService.findProductsByIds(productIds);
            return ResponseEntity.ok(product);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    private boolean isImageFile(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null && contentType.startsWith("image/");
    }


    @DeleteMapping("/soft-delete/{id}")
    public ResponseEntity<String> softDeleteProduct(@PathVariable("id") Long productId) {
        productService.softDeleteProduct(productId);
        return ResponseEntity.ok("Product soft deleted successfully");
    }

    @PutMapping("/restore/{id}")
    public ResponseEntity<String> restoreProduct(@PathVariable("id") Long productId) {
        productService.restoreProduct(productId);
        return ResponseEntity.ok("Product restored successfully");
    }



    //@PostMapping("/generateFakeProducts")
    private ResponseEntity<String> generateFakeProducts() {
        Faker faker = new Faker();
        for (int i = 0; i < 100_000; i++) {
            String productName = faker.commerce().productName();
            if (productService.existsByName(productName)) {
                continue;
            }
            ProductDTO productDTO = ProductDTO.builder().name(productName).price((float) faker.number().numberBetween(10, 90_000_000)).description(faker.lorem().sentence()).thumbnail("").categoryId((long) faker.number().numberBetween(2, 5)).build();
            try {
                productService.createProduct(productDTO);
            } catch (DataNotFoundException e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }
        return ResponseEntity.ok("Fake products create successfully");
    }
}
