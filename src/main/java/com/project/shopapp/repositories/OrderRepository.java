package com.project.shopapp.repositories;

import com.project.shopapp.models.OrderModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<OrderModel , Long> {
    //Tìm các đơn hàng của 1 order nào đó
    Page<OrderModel> findByUserId(Long userId, Pageable pageable);
}
