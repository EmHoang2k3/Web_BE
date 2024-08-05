package com.project.shopapp.service;

import com.project.shopapp.dtos.OrderDetailDTO;
import com.project.shopapp.exceptions.DataNotFoundException;
import com.project.shopapp.models.OrderDetailModel;

import java.util.List;

public interface IOrderDetailService {


    OrderDetailModel createOrderDetail(OrderDetailDTO newOrderDetail) throws Exception;

    OrderDetailModel getOrderDetail(Long id) throws DataNotFoundException;

    OrderDetailModel updateOrderDetail(Long id, OrderDetailDTO newOrderDetail);

    void deleteOrderDetail(Long id);

    List<OrderDetailModel> findByOrderId (Long orderId);

}
