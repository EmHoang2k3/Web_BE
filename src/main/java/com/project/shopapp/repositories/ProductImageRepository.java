package com.project.shopapp.repositories;

import com.project.shopapp.models.ProductImageModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductImageRepository extends JpaRepository<ProductImageModel,Long> {
    List<ProductImageModel> findByProductId(Long productId);
}
