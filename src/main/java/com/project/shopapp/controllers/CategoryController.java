package com.project.shopapp.controllers;

import com.project.shopapp.components.LocalizationUtils;
import com.project.shopapp.dtos.CategoryDTO;
import com.project.shopapp.models.CategoryModel;
import com.project.shopapp.responses.CategoryListResponse;
import com.project.shopapp.responses.CategoryResponse;
import com.project.shopapp.service.CategoryService;
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
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

@RestController
@RequestMapping("${api.prefix}/categories")
@RequiredArgsConstructor
//@Validated
public class CategoryController {

    private final CategoryService categoryService;
    private final LocalizationUtils localizationUtils;
    @PostMapping(value = "",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createCategory(@ModelAttribute CategoryDTO categoryDTO,
                                            @RequestParam("file") MultipartFile file){
        try {
            // Kiểm tra và lưu file
            if (file != null && !file.isEmpty()) {
                String filename = storeFile(file); // Hàm lưu file bạn đã có
                categoryDTO.setImageThumbnail(filename); // Lưu tên file vào DTO
            }
            categoryService.createCategory(categoryDTO); // Tạo category
            return ResponseEntity.ok(localizationUtils.getLocalizedMessage(MessageKeys.INSERT_CATEGORY_SUCCESSFULLY));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //Hiển thị tất cả category
    @GetMapping("")
    //http://localhost:8088/api/v1/categories?page=1&limit=10
    public ResponseEntity<CategoryListResponse> GetAllCategories(
            @RequestParam("page")    int page,
            @RequestParam("limit")   int limit
    ){

       PageRequest pageRequest = PageRequest.of(page - 1, limit, Sort.by("id").ascending());
        Page categoryPage = categoryService.getAllCategory(pageRequest);
        int totalPage = categoryPage.getTotalPages();
      List<CategoryResponse> categories = categoryPage.getContent();
        return ResponseEntity.ok(CategoryListResponse.builder()
                .category(categories)
                .totalPages(totalPage)
                .build());
    }

    @PutMapping(value = "/{id}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CategoryResponse> updateCategory(@PathVariable Long id,
                                                           @ModelAttribute @Valid CategoryDTO categoryDTO,
                                                           @RequestParam(value = "file", required = false) MultipartFile file){
        try {
            if (file != null && !file.isEmpty()) {
                String filename = storeFile(file); // Hàm lưu file
                categoryDTO.setImageThumbnail(filename);
            }
            CategoryModel categoryUpdate = categoryService.updateCategory(id, categoryDTO);
            return ResponseEntity.ok(CategoryResponse.formCategory(categoryUpdate));
        } catch (Exception e) {
            e.printStackTrace(); // In ra log lỗi chi tiết
            return ResponseEntity.badRequest().body(CategoryResponse.builder()
                    .message(e.getMessage())
                    .build());
        }

    }

    @DeleteMapping("/{id}")

    public ResponseEntity<String> deleteCategory(@PathVariable Long id){
        categoryService.remoteCategory(id);
        return ResponseEntity.status(HttpStatus.OK).body(localizationUtils.getLocalizedMessage(MessageKeys.DELETE_CATEGORY_SUCCESSFULLY));
    }





    @PostMapping(value = "/upload/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadImage(@PathVariable("id") Long categoryId, @RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(localizationUtils.getLocalizedMessage(MessageKeys.UPLOAD_IMAGES_MAX_1));
            }

            // Kiểm tra kích thước và loại file
            if (file.getSize() > 10 * 1024 * 1024) { // Kích thước > 10MB
                return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
                        .body(localizationUtils.getLocalizedMessage(MessageKeys.UPLOAD_IMAGE_FILE_LARGE));
            }

            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                        .body(localizationUtils.getLocalizedMessage(MessageKeys.UPLOAD_IMAGES_FILE_MUST_BE_IMAGE));
            }

            // Lưu file và cập nhật trong Category
            String filename = storeFile(file);
            CategoryModel updatedCategory = categoryService.updateCategoryImage(categoryId, filename);

            return ResponseEntity.ok(updatedCategory);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity<CategoryModel> getCategoryDetail(@PathVariable Long id) throws Exception{
        CategoryModel categoryDetail = categoryService.getCategoryById(id);
        return ResponseEntity.ok(categoryDetail);
    }

    @GetMapping("/images/{imageName}")
    public ResponseEntity<?> viewImage(@PathVariable String imageName) throws Exception {
        try {
            java.nio.file.Path imagePath = Paths.get("uploads_categories/" + imageName);
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


    private String storeFile(MultipartFile file) throws IOException {

        if (!isImageFile(file) || file.getOriginalFilename() == null) {
            throw new IOException("Invalid image format");
        }
        String filename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        //Thêm UUID vào trước tên file để đảm bảo tên file là duy nhất
        String uniqueFilename = UUID.randomUUID().toString() + "_" + filename;
        //Đường dẫn đến thư mục bạn muốn lưu file
        java.nio.file.Path uploadDir = Paths.get("uploads_categories");
        //Kiểm tra và tạo thư mục nếu nó không tồn tại
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }
        //Đường dẫn đầy đủ đến file
        java.nio.file.Path destination = Paths.get(uploadDir.toString(), uniqueFilename);
        //Sao chép file vào thư mục đích
        Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
        return uniqueFilename;
    }

    private boolean isImageFile(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null && contentType.startsWith("image/");
    }









}
