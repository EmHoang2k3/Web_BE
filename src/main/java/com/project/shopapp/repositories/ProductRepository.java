package com.project.shopapp.repositories;

import com.project.shopapp.models.ProductModel;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import java.awt.print.Pageable;

public interface ProductRepository extends JpaRepository<ProductModel,Long> {

    boolean existsByName(String name);

    //phân trang
    Page<ProductModel> findAll(Pageable pageable);

}
