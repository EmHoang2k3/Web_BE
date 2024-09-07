package com.project.shopapp.controllers;


import com.project.shopapp.components.LocalizationUtils;
import com.project.shopapp.dtos.OrderDTO;
import com.project.shopapp.models.OrderModel;
import com.project.shopapp.responses.OrderHistoryResponse;
import com.project.shopapp.responses.OrderResponse;
import com.project.shopapp.service.IOrderService;
import com.project.shopapp.service.OrderService;
import com.project.shopapp.utils.MessageKeys;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final LocalizationUtils localizationUtils;
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
    public ResponseEntity<OrderHistoryResponse> getOrders(@PathVariable("user_id") Long userId,
                                                          @RequestParam(value = "page") int page,
                                                          @RequestParam(value = "limit") int limit
    ){

        try {
            PageRequest pageRequest = PageRequest.of( page - 1, limit, Sort.by("orderDate").ascending());
            Page orderGetIdPage = orderService.findByUserId(userId,pageRequest);
            int totalPage = orderGetIdPage.getTotalPages();
            List<OrderResponse> orderResponses = orderGetIdPage.getContent();
            return ResponseEntity.ok(OrderHistoryResponse.builder()
                    .orders(orderResponses)
                    .totalPages(totalPage)
                    .build());

        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    @GetMapping("/{id}")
    //Get: http://localhost:8088/api/v1/orders/1
    public ResponseEntity<?> getOrder(@Valid @PathVariable("id") Long orderId){

        try{
             OrderModel existingOrder = orderService.getOrder(orderId);
            OrderResponse orderResponse = OrderResponse.formOrder(existingOrder);
            return ResponseEntity.ok(orderResponse);
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
        return ResponseEntity.ok(localizationUtils.getLocalizedMessage(MessageKeys.DELETE_ORDER_SUCCESSFULLY));
    }
}
