package com.project.shopapp.repositories;

import com.project.shopapp.models.ProductImageModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductImageRepository extends JpaRepository<ProductImageModel,Long> {
    List<ProductImageModel> findByProductId(Long productId);

    @Query("SELECT COUNT(pi) FROM ProductImageModel pi WHERE pi.product.id = :productId")
    int countByProductId(@Param("productId") Long productId);

//    @Query("SELECT pi FROM ProductImageModel pi WHERE pi.product.id = :productId")
//    List<ProductImageModel> findByProductId(@Param("productId") Long productId);
}
