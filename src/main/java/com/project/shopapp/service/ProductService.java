package com.project.shopapp.service;

import com.project.shopapp.components.LocalizationUtils;
import com.project.shopapp.dtos.ProductDTO;
import com.project.shopapp.dtos.ProductImageDTO;
import com.project.shopapp.exceptions.DataNotFoundException;
import com.project.shopapp.exceptions.InvalidParamException;
import com.project.shopapp.exceptions.ResourceNotFoundException;
import com.project.shopapp.models.CategoryModel;
import com.project.shopapp.models.OrderModel;
import com.project.shopapp.models.ProductImageModel;
import com.project.shopapp.models.ProductModel;
import com.project.shopapp.repositories.CategoryRepository;
import com.project.shopapp.repositories.ProductImageRepository;
import com.project.shopapp.repositories.ProductRepository;
import com.project.shopapp.responses.ProductResponse;
import com.project.shopapp.utils.MessageKeys;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {

    private final ProductImageService productImageService;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductImageRepository productImageRepository;
    private final LocalizationUtils localizationUtils;
    @Override
    public ProductModel createProduct(ProductDTO productDTO) throws DataNotFoundException {
        CategoryModel existingCategory = categoryRepository
                .findById(productDTO.getCategoryId())
                .orElseThrow(() ->
                        new DataNotFoundException("Can not category with id: " + productDTO.getCategoryId()));
        ProductModel newProduct = ProductModel.builder()
                .name(productDTO.getName())
                .price(productDTO.getPrice())
                .thumbnail(productDTO.getThumbnail())
                .description(productDTO.getDescription())
                .category(existingCategory)
                .build();
        return productRepository.save(newProduct);
    }


    public ProductDTO getProductDetail(Long productId) {
        ProductModel product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        List<ProductImageModel> images = productImageRepository.findByProductId(productId);


        List<ProductImageDTO> imageDTOs = images.stream()
                .map(image -> ProductImageDTO.builder()
                        .id(image.getId())
                        .imageUrl(image.getImageUrl())
                        .build())
                .collect(Collectors.toList());

        return ProductDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .thumbnail(product.getThumbnail())
                .description(product.getDescription())
                .categoryId(product.getCategory().getId())  // Assuming `getName()` exists in `CategoryModel`
                .product_images(imageDTOs)
                .build();
    }


    @Override
    public ProductModel getProductById(Long id) throws DataNotFoundException {
        return productRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Cannot find product with id: " + id));
    }


    @Override
    public List<ProductModel> findProductsByIds(List<Long> productIds) {
        return productRepository.findProductsByIds(productIds);
    }

    @Override
    public Page getAllProduct(String keyword, Long categoryId, PageRequest pageRequest) {
        //Lấy danh sách sản phẩm theo page và limit
        Page<ProductModel> productPage = productRepository.findByActiveTrue(categoryId, keyword, pageRequest);
        // Debug thông tin về trang và tổng số phần tử
        return productPage.map(ProductResponse::formProduct);
    }

    @Override
    public Page<ProductModel> getProductsByCategory(Long categoryId, PageRequest pageRequest) {
        return productRepository.findByCategory_Id(categoryId, pageRequest);
    }

    @Override
    @Transactional
    public ProductModel updateProducts(
            long id,
            ProductDTO productDTO) throws Exception {

        ProductModel existingProduct = getProductById(id);
        if (existingProduct != null) {
            CategoryModel existingCategory = categoryRepository
                    .findById(productDTO.getCategoryId())
                    .orElseThrow(() ->
                            new DataNotFoundException("Can not category with id: " + productDTO.getCategoryId()));
            existingProduct.setName(productDTO.getName());
            existingProduct.setPrice(productDTO.getPrice());
            existingProduct.setCategory(existingCategory);
            existingProduct.setDescription(productDTO.getDescription());
            existingProduct.setThumbnail(productDTO.getThumbnail());
            return productRepository.save(existingProduct);
        }
        return null;
    }

    @Override
    public void deleteProduct(long id) {
        Optional<ProductModel> optionalProduct = productRepository.findById(id);
        optionalProduct.ifPresent(productRepository::delete);
    }

    @Override
    public boolean existsByName(String name) {
        return productRepository.existsByName(name);
    }

    @Override
    public ProductImageModel createProductImage(
            Long id,
            ProductImageDTO productImageDTO) throws Exception {
        ProductModel existingProduct = productRepository
                .findById(id)
                .orElseThrow(() -> new DataNotFoundException("Product not found"));

        // Kiểm tra số lượng hình ảnh trước khi khởi tạo đối tượng mới
        int size = productImageRepository.countByProductId(existingProduct.getId());
        if (size >= ProductImageModel.MAXIMUM_IMAGES_PER_PRODUCT) {
            throw new InvalidParamException("Number of images must be <= "
                    + ProductImageModel.MAXIMUM_IMAGES_PER_PRODUCT);
        }

        // Tạo ProductImageModel sau khi kiểm tra
        ProductImageModel newProductImage = ProductImageModel.builder()
                .product(existingProduct)
                .imageUrl(productImageDTO.getImageUrl())
                .build();

        return productImageRepository.save(newProductImage);
    }

    @Override
    @Transactional
    public List<ProductImageModel> saveProductImage(ProductModel product, List<MultipartFile> files) throws Exception {
        List<ProductImageModel> productImages = new ArrayList<>();
        String thumbnailImage = null;

        for (int i = 0; i < files.size(); i++) {
            MultipartFile file = files.get(i);

            // Kiểm tra kích thước file
            if (file.getSize() > 10 * 1024 * 1024) {
                throw new Exception(localizationUtils.getLocalizedMessage(MessageKeys.UPLOAD_IMAGE_FILE_LARGE));
            }

            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                throw new Exception(localizationUtils.getLocalizedMessage(MessageKeys.UPLOAD_IMAGES_FILE_MUST_BE_IMAGE));
            }

            // Lưu file
            String fileName = storeFile(file);

            // Lưu ảnh vào database
            ProductImageModel productImage = createProductImage(product.getId(), ProductImageDTO.builder()
                    .imageUrl(fileName)
                    .build());
            productImages.add(productImage);

            // Đặt ảnh đầu tiên làm thumbnail
            if (i == 0) {
                thumbnailImage = fileName;
            }
        }

        // Cập nhật thumbnail
        if (thumbnailImage != null) {
            product.setThumbnail(thumbnailImage);
            updateProduct(product);
        }

        return productImages;
    }

    @Override
    @Transactional
    public List<ProductImageModel> saveProductImagesForUpdate(ProductModel product, List<MultipartFile> files) throws Exception {
        // Nếu không có ảnh tải lên, giữ nguyên ảnh cũ
        if (files == null || files.isEmpty()) {
            return productImageRepository.findByProductId(product.getId());
        }

        // Nếu có ảnh mới tải lên, xóa tất cả ảnh cũ trước khi lưu ảnh mới
        productImageService.deleteAllImagesByProductId(product.getId());

        List<ProductImageModel> productImages = new ArrayList<>();
        String thumbnailImage = null;

        for (int i = 0; i < files.size(); i++) {
            MultipartFile file = files.get(i);

            // Kiểm tra kích thước file
            if (file.getSize() > 10 * 1024 * 1024) {
                throw new Exception(localizationUtils.getLocalizedMessage(MessageKeys.UPLOAD_IMAGE_FILE_LARGE));
            }

            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                throw new Exception(localizationUtils.getLocalizedMessage(MessageKeys.UPLOAD_IMAGES_FILE_MUST_BE_IMAGE));
            }

            // Lưu file
            String fileName = storeFile(file);

            // Lưu ảnh vào database
            ProductImageModel productImage = createProductImage(product.getId(), ProductImageDTO.builder()
                    .imageUrl(fileName)
                    .build());
            productImages.add(productImage);

            // Đặt ảnh đầu tiên làm thumbnail
            if (i == 0) {
                thumbnailImage = fileName;
            }
        }

        // Cập nhật thumbnail nếu có ảnh mới
        if (thumbnailImage != null) {
            product.setThumbnail(thumbnailImage);
            updateProduct(product);
        }

        return productImages;
    }


    public void updateProduct(ProductModel product) {
        productRepository.save(product);
    }

    private String storeFile(MultipartFile file) throws IOException {

        if (file.getOriginalFilename() == null) {
            throw new IOException("Invalid image format");
        }
        String filename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        //Thêm UUID vào trước tên file để đảm bảo tên file là duy nhất
        String uniqueFilename = UUID.randomUUID().toString() + "_" + filename;
        //Đường dẫn đến thư mục bạn muốn lưu file
        java.nio.file.Path uploadDir = Paths.get("uploads");
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

    @Override
    public void softDeleteProduct(long productId) {
        ProductModel product = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Product not found"));
        product.setActive(false);
        productRepository.save(product);
    }

    public void restoreProduct(Long productId) {
        ProductModel product = productRepository.findById(productId).orElse(null);
        if (product != null && !product.getActive()) {
            product.setActive(true);  // Khôi phục sản phẩm
            productRepository.save(product);
        } else {
            throw new RuntimeException("Product not found or already active");
        }
    }

    public Page<ProductModel> getSoftDeletedProducts(PageRequest pageRequest) {
        return productRepository.findSoftDeletedProducts(pageRequest);
    }
}
