package com.project.shopapp.repositories;

import com.project.shopapp.models.CategoryModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<CategoryModel,Long> {

    Page<CategoryModel> findAll(Pageable pageable);
}
