package com.project.shopapp.repositories;

import com.project.shopapp.models.ProductModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface ProductRepository extends JpaRepository<ProductModel,Long> {

    boolean existsByName(String name);

    //phân trang
    //Page<ProductModel> findAll(Pageable pageable);


    @Query("SELECT p FROM ProductModel p WHERE " +
            "(p.category.id = :categoryId OR :categoryId IS NULL OR :categoryId = 0) " +
            "AND (p.name LIKE %:keyword% OR p.description LIKE %:keyword% OR :keyword IS NULL OR :keyword = '')")
    Page<ProductModel> searchProducts(@Param("categoryId") Long categoryId,
                                      @Param("keyword") String keyword, Pageable pageable);

}
