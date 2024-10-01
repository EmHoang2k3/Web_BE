package com.project.shopapp.service;



import com.project.shopapp.dtos.OrderDTO;
import com.project.shopapp.exceptions.DataNotFoundException;
import com.project.shopapp.models.OrderModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;


public interface IOrderService {
    OrderModel createOrder (OrderDTO orderDTO) throws Exception;

    OrderModel getOrder (long id);


//    List<OrderModel> findByUserId(Long userId);

    Page findByUserId(Long userId, PageRequest pageRequest);

    OrderModel updateOrder(long id, OrderDTO orderDTO) throws DataNotFoundException;

    OrderModel updateOrderStatus(long orderId, String newStatus) throws Exception;

    Page<OrderModel> getAllOrder(PageRequest pageRequest);

    void remoteOrder(long id);
}
