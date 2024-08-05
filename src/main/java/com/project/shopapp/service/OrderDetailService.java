package com.project.shopapp.service;

import com.project.shopapp.dtos.OrderDTO;
import com.project.shopapp.dtos.OrderDetailDTO;
import com.project.shopapp.exceptions.DataNotFoundException;
import com.project.shopapp.models.OrderDetailModel;
import com.project.shopapp.models.OrderModel;
import com.project.shopapp.models.ProductModel;
import com.project.shopapp.repositories.OrderDetailRepository;
import com.project.shopapp.repositories.OrderRepository;
import com.project.shopapp.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderDetailService implements IOrderDetailService{

    private final OrderRepository orderRepository;

    private final OrderDetailRepository orderDetailRepository;

    private final ProductRepository productRepository;


    @Override
    public OrderDetailModel createOrderDetail(OrderDetailDTO orderDetailDTO) throws Exception {
        //Tìm orderId xem có tồn tại hay không
        OrderModel order = orderRepository.findById(orderDetailDTO.getOrderId())
                .orElseThrow(()-> new DataNotFoundException("Cannot find Order with id: "+orderDetailDTO.getOrderId()));
        //Tìm product theo id
        ProductModel product = productRepository.findById(orderDetailDTO.getProductId())
                .orElseThrow(()-> new DataNotFoundException("Cannot find Product with id: "+orderDetailDTO.getProductId()));

        OrderDetailModel orderDetail = OrderDetailModel.builder()
                .order(order)
                .product(product)
                .numberOfProduct(orderDetailDTO.getNumberOfProduct())
                .color(orderDetailDTO.getColor())
                .totalMoney(orderDetailDTO.getTotalMoney())
                .build();
        return orderDetailRepository.save(orderDetail);

    }

    @Override
    public OrderDetailModel getOrderDetail(Long id) throws DataNotFoundException {
        return orderDetailRepository.findById(id)
                .orElseThrow(()->new DataNotFoundException("Cannot find OrderDetailId width id: "+id));
    }

    @Override
    public OrderDetailModel updateOrderDetail(Long id, OrderDetailDTO newOrderDetail) {
        return null;
    }

    @Override
    public void deleteOrderDetail(Long id) {
        orderDetailRepository.deleteById(id);
    }

    @Override
    public List<OrderDetailModel> findByOrderId(Long orderId) {
        return orderDetailRepository.findByOrderId(orderId);
    }
}
