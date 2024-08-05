package com.project.shopapp.controllers;


import com.project.shopapp.dtos.OrderDTO;
import com.project.shopapp.models.OrderModel;
import com.project.shopapp.service.IOrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/orders")
@RequiredArgsConstructor
public class OrderController {

    private final IOrderService orderService;

    @PostMapping("")
    public ResponseEntity<?> createOrder(@RequestBody @Valid
                                              OrderDTO orderDTO,
                                              BindingResult result){
        try{
            if(result.hasErrors()){
                List<String> errorMessage = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessage);
            }
            OrderModel orderResponse = orderService.createOrder(orderDTO);
            return ResponseEntity.ok(orderResponse);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @GetMapping("/user/{user_id}")
    //Get: http://localhost:8088/api/v1/orders/user/4
    public ResponseEntity<?> getOrders(@Valid @PathVariable("user_id") Long userId){

        try{
            List<OrderModel> orders = orderService.findByUserId(userId);
            return ResponseEntity.ok(orders);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @GetMapping("/{id}")
    //Get: http://localhost:8088/api/v1/orders/1
    public ResponseEntity<?> getOrder(@Valid @PathVariable("id") Long orderId){

        try{
             OrderModel existingOrder = orderService.getOrder(orderId);
            return ResponseEntity.ok(existingOrder);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }



    @PutMapping("/{id}")
    //Công việc của admin
    public ResponseEntity<?> updateOrder(
            @Valid @PathVariable long id,
            @Valid @RequestBody OrderDTO orderDTO
    ){
        try{
            OrderModel order = orderService.updateOrder(id, orderDTO);
            return ResponseEntity.ok(order);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    //Công việc của admin
    public ResponseEntity<?> deleteOrder(
            @Valid @PathVariable long id
    ){
        //Xóa mềm => cập nhật trường active = false
        orderService.remoteOrder(id);
        return ResponseEntity.ok("Delete order successfully");
    }
}
