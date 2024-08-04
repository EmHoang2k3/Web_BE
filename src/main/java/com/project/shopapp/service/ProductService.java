package com.project.shopapp.service;

import com.project.shopapp.dtos.ProductDTO;
import com.project.shopapp.dtos.ProductImageDTO;
import com.project.shopapp.exceptions.DataNotFoundException;
import com.project.shopapp.exceptions.InvalidParamException;
import com.project.shopapp.models.CategoryModel;
import com.project.shopapp.models.ProductImageModel;
import com.project.shopapp.models.ProductModel;
import com.project.shopapp.repositories.CategoryRepository;
import com.project.shopapp.repositories.ProductImageRepository;
import com.project.shopapp.repositories.ProductRepository;
import com.project.shopapp.responses.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class ProductService implements IProductService{


    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductImageRepository productImageRepository;

    @Override
    public ProductModel createProduct(ProductDTO productDTO) throws DataNotFoundException {
        CategoryModel existingCategory = categoryRepository
                .findById(productDTO.getCategoryId())
                .orElseThrow(()->
                        new DataNotFoundException("Can not category with id: "+productDTO.getCategoryId()));
        ProductModel newProduct = ProductModel.builder()
                .name(productDTO.getName())
                .price(productDTO.getPrice())
                .thumbnail(productDTO.getThumbnail())
                .description(productDTO.getDescription())
                .category(existingCategory)
                .build();
        return productRepository.save(newProduct);
    }

    @Override
    public ProductModel getProductById(long id) throws Exception {
        return productRepository.findById(id)
                .orElseThrow(()-> new DataNotFoundException("Cannot find product with id is :" + id));
    }

    @Override
    public Page<ProductResponse> getAllProduct(PageRequest pageRequest) {
        //Lấy danh sách sản phẩm theo page và limit
        return productRepository.findAll(pageRequest).map(ProductResponse::formProduct);
    }

    @Override
    public ProductModel updateProduct(
            long id,
            ProductDTO productDTO) throws Exception {

        ProductModel existingProduct = getProductById(id);
        if(existingProduct != null){
            CategoryModel existingCategory = categoryRepository
                    .findById(productDTO.getCategoryId())
                    .orElseThrow(()->
                            new DataNotFoundException("Can not category with id: "+productDTO.getCategoryId()));
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
        optionalProduct.ifPresent(productRepository :: delete);
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
                .orElseThrow(()->
                        new DataNotFoundException("Can not category with id: "+productImageDTO.getProductId()));
        ProductImageModel newProductImage = ProductImageModel.builder()

                .product(existingProduct)
                .imageUrl(productImageDTO.getImageUrl())
                .build();
        //Cannot insert > 5 image into 1 product
        int size = productImageRepository.findByProductId(existingProduct.getId()).size();
        if(size >= ProductImageModel.MAXIMUM_IMAGES_PER_PRODUCT){

            throw new InvalidParamException("" +
                    "Number of images must be <= "
                    +ProductImageModel.MAXIMUM_IMAGES_PER_PRODUCT);

        }
        return productImageRepository.save(newProductImage);
    }
}
