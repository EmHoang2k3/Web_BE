package com.project.shopapp.service;



import com.project.shopapp.dtos.OrderDTO;
import com.project.shopapp.repositories.OrderRepository;
import com.project.shopapp.responses.OrderResponse;

import java.util.List;

public interface IOrderService {
    OrderResponse createOrder (OrderDTO orderDTO);

    OrderResponse getOrder (long id);

    List<OrderResponse> getAllOrder(Long userId);

    OrderResponse updateOrder(long id, OrderDTO orderDTO);

    void remoteCategory(long id);
}
