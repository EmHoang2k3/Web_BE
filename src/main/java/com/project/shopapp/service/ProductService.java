package com.project.shopapp.service;

import com.project.shopapp.dtos.ProductDTO;
import com.project.shopapp.models.ProductModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public class ProductService implements IProductService{
    @Override
    public ProductModel createProduct(ProductDTO productDTO) {
        return null;
    }

    @Override
    public ProductModel getProductById(long id) {
        return null;
    }

    @Override
    public Page<ProductModel> getAllProduct(PageRequest pageRequest) {
        return null;
    }

    @Override
    public ProductModel updateProduct(long id, ProductDTO productDTO) {
        return null;
    }

    @Override
    public void deleteProduct(long id) {

    }

    @Override
    public boolean existsByName(String name) {
        return false;
    }
}
