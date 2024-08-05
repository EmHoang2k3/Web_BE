package com.project.shopapp.service;



import com.project.shopapp.dtos.OrderDTO;
import com.project.shopapp.exceptions.DataNotFoundException;
import com.project.shopapp.models.OrderModel;

import java.util.List;

public interface IOrderService {
    OrderModel createOrder (OrderDTO orderDTO) throws Exception;

    OrderModel getOrder (long id);

    List<OrderModel> findByUserId(Long userId);

    OrderModel updateOrder(long id, OrderDTO orderDTO) throws DataNotFoundException;

    void remoteOrder(long id);
}
