package com.project.shopapp.controllers;

import com.project.shopapp.dtos.OrderDetailDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/order-detail")
public class OrderDetailController {

    @PostMapping("")
    public ResponseEntity<?> createOrderDetail(@Valid @RequestBody OrderDetailDTO orderDetailDTO){
        return ResponseEntity.ok("createOrderDetail here :>");
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderDetail(
            @Valid @PathVariable("id") Long id
    ){
        return ResponseEntity.ok("getOrderDetail with id" +id);
    }

    //Lấy ra danh sach order detail của 1 order nào đó
//    @GetMapping("/order/{order_id}")
//    public ResponseEntity<?> getOrderDetails(@Valid @PathVariable("orderId") Long orderId){
////        List<OrderDetailDTO> orderDetailDTOS = orderDetailService.getOrderDetails(orderId);
//        return ResponseEntity.ok(orderId);
//    }
    @GetMapping("/order/{orderId}")
    public ResponseEntity<?> getOrderDetails(@Valid @PathVariable("orderId") Long orderId){
        return ResponseEntity.ok(orderId);
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

