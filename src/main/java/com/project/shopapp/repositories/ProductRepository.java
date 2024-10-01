package com.project.shopapp.repositories;

import com.project.shopapp.models.ProductModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface ProductRepository extends JpaRepository<ProductModel,Long> {

    boolean existsByName(String name);

    List<ProductModel> findByActiveTrue();
    //phân trang
    //Page<ProductModel> findAll(Pageable pageable);


    @Query("SELECT p FROM ProductModel p WHERE " +
            "p.active = true AND " +  // Thêm điều kiện kiểm tra active
            "(p.category.id = :categoryId OR :categoryId IS NULL OR :categoryId = 0) " +
            "AND (p.name LIKE %:keyword% OR p.description LIKE %:keyword% OR :keyword IS NULL OR :keyword = '')")
    Page<ProductModel> findByActiveTrue(@Param("categoryId") Long categoryId,
                                        @Param("keyword") String keyword,
                                        Pageable pageable);

    @Query("SELECT p FROM ProductModel p WHERE p.id IN :productIds")
    List<ProductModel> findProductsByIds(@Param("productIds") List<Long> productIds);


    @Query("SELECT p FROM ProductModel p WHERE p.active = false")
    Page<ProductModel> findSoftDeletedProducts(Pageable pageable);


    Page<ProductModel> findByCategory_Id(Long categoryId , Pageable pageable);
}
