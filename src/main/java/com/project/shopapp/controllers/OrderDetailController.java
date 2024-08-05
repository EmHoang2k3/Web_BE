package com.project.shopapp.controllers;

import com.project.shopapp.dtos.OrderDetailDTO;
import com.project.shopapp.exceptions.DataNotFoundException;
import com.project.shopapp.models.OrderDetailModel;
import com.project.shopapp.service.OrderDetailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/order-detail")
@RequiredArgsConstructor
public class OrderDetailController {

    private final OrderDetailService orderDetailService;


    @PostMapping("")
    public ResponseEntity<?> createOrderDetail(@Valid @RequestBody OrderDetailDTO orderDetailDTO){
        try {
            OrderDetailModel orderDetail = orderDetailService.createOrderDetail(orderDetailDTO);
            return ResponseEntity.ok().body(orderDetail);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderDetail(
            @Valid @PathVariable("id") Long id
    ) throws DataNotFoundException {
        OrderDetailModel orderDetail = orderDetailService.getOrderDetail(id);
        return ResponseEntity.ok(orderDetail);
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<?> getOrderDetails(@Valid @PathVariable("orderId") Long orderId){
         List<OrderDetailModel> orderDetails = orderDetailService.findByOrderId(orderId);
        return ResponseEntity.ok(orderDetails);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrderDetail(
            @Valid @PathVariable("id") Long id,
            @RequestBody OrderDetailDTO orderDetailDTO
    ){
        return ResponseEntity.ok("updateOrderDetail with id" +id +", orderDetailDTO"+orderDetailDTO );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDetail(
            @Valid @PathVariable("id") Long id
    ){
        return ResponseEntity.noContent().build();
    }
}

