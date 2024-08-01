package com.project.shopapp.service;

import com.project.shopapp.dtos.ProductDTO;
import com.project.shopapp.models.ProductModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public interface IProductService {
    public ProductModel createProduct(ProductDTO productDTO);

    ProductModel getProductById(long id);

    Page<ProductModel> getAllProduct(PageRequest pageRequest);

    ProductModel updateProduct(long id,ProductDTO productDTO);

    void deleteProduct (long id);

    boolean existsByName(String name);
}
