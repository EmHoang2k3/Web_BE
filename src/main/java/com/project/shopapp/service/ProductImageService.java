package com.project.shopapp.service;

import com.project.shopapp.repositories.ProductImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductImageService {

    @Autowired
    private ProductImageRepository productImageRepository;

    public void deleteAllImagesByProductId(Long productId) {
        productImageRepository.deleteByProductId(productId);
    }
}
