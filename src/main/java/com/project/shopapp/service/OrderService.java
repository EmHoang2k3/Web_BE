package com.project.shopapp.service;

import com.project.shopapp.dtos.CartItemDTO;
import com.project.shopapp.dtos.OrderDTO;
import com.project.shopapp.exceptions.DataNotFoundException;
import com.project.shopapp.models.*;
import com.project.shopapp.repositories.OrderDetailRepository;
import com.project.shopapp.repositories.OrderRepository;
import com.project.shopapp.repositories.ProductRepository;
import com.project.shopapp.repositories.UserRepository;
import com.project.shopapp.responses.OrderResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService{
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final ModelMapper modelMapper;
    @Override
    @Transactional
    public OrderModel createOrder(OrderDTO orderDTO) throws Exception {
        //Kiểm tra userId có tồn tại hay không
        UserModel user = userRepository
                .findById(orderDTO.getUserId())
                .orElseThrow(()-> new DataNotFoundException("Cannot file user width id : "+orderDTO.getUserId()));

        //Convert OrderDto -> OrderModel
        //Tạo luồng bằng ánh xạ riêng để kiểm soát việc ánh xạ
        modelMapper.typeMap(OrderDTO.class, OrderModel.class)
                .addMappings(mapper -> mapper.skip(OrderModel::setId));

        //Cập nhật các trường của đơn hàng từ OrderDTO
        OrderModel order = new OrderModel();
        modelMapper.map(orderDTO,order);
        order.setUser(user);
        order.setOrderDate(new Date());//Thời điểm hiện tại
        order.setStatus(OrderStatus.PENDING);
        //Kiểm tra shipping date phải >= ngày hôm nay
        LocalDate shippingDate = orderDTO.getShippingDate() == null ? LocalDate.now() : orderDTO.getShippingDate();
        if(shippingDate.isBefore(LocalDate.now())){
            throw new DataNotFoundException("Date must be at least today");
        }
        order.setShippingDate(shippingDate);
        order.setActive(true);
        order.setTotalMoney(orderDTO.getTotalMoney());
        //Lưu vào trong Database
        orderRepository.save(order);

        List<OrderDetailModel> orderDetails = new ArrayList<>();
        for(CartItemDTO cartItemDTO: orderDTO.getCartItems()){
            OrderDetailModel orderDetail = new OrderDetailModel();
            orderDetail.setOrder(order);
            Long productId = cartItemDTO.getProductId();
            int quantity = cartItemDTO.getQuantity();

            ProductModel product = productRepository.findById(productId)
                    .orElseThrow(() -> new DataNotFoundException("Product not found width id: "+productId));

            orderDetail.setProduct(product);
            orderDetail.setNumberOfProduct(quantity);
            orderDetail.setPrice(product.getPrice());
            orderDetails.add(orderDetail);
        }
        orderDetailRepository.saveAll(orderDetails);
        return order;
    }

    @Override
    public OrderModel getOrder(long id) {
      OrderModel selectedOrder = orderRepository.findById(id).orElse(null);
      return selectedOrder;
    }

    @Override
    public Page findByUserId(Long userId, PageRequest pageRequest) {
        Page<OrderModel> orderGetIdPage = orderRepository.findByUserId(userId,pageRequest);
        return orderGetIdPage.map(OrderResponse :: formOrder);
    }

    @Override
    @Transactional
    public OrderModel updateOrder(long id, OrderDTO orderDTO) throws DataNotFoundException {
        OrderModel order = orderRepository.findById(id).orElseThrow(()->
                new DataNotFoundException("Cannot find order with id: "+ id));
        UserModel existingUser = userRepository.findById(orderDTO.getUserId()).orElseThrow(()->
                new DataNotFoundException("Cannot find user with id: "+ orderDTO.getUserId()));

        modelMapper.typeMap(OrderDTO.class,OrderModel.class).addMappings(mapper -> mapper.skip(OrderModel::setId));

        modelMapper.map(orderDTO,order);
        order.setUser(existingUser);
        return orderRepository.save(order);

    }

    @Override
    public void remoteOrder(long id) {
        OrderModel order = orderRepository.findById(id).orElse(null);
        //Không xóa cứng, => please soft-delete
        if(order != null){
            order.setActive(false);
            orderRepository.save(order);
        }
    }
}
