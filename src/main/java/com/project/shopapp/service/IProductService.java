package com.project.shopapp.service;

import com.project.shopapp.dtos.ProductDTO;
import com.project.shopapp.dtos.ProductImageDTO;
import com.project.shopapp.exceptions.DataNotFoundException;
import com.project.shopapp.models.ProductImageModel;
import com.project.shopapp.models.ProductModel;
import com.project.shopapp.responses.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface IProductService {
    public ProductModel createProduct(ProductDTO productDTO) throws DataNotFoundException;

    ProductModel getProductById(long id) throws Exception;


    ProductModel getProductById(Long id) throws DataNotFoundException;

    List<ProductModel> findProductsByIds(List<Long> productIds);

    Page<ProductResponse> getAllProduct(String keyword, Long categoryId, PageRequest pageRequest);

    ProductModel updateProduct(long id,ProductDTO productDTO) throws Exception;

    void deleteProduct (long id);

    boolean existsByName(String name);

    public ProductImageModel createProductImage(
            Long id,
            ProductImageDTO productImageDTO) throws Exception;
}
