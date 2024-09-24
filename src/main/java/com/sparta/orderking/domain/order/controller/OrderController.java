package com.sparta.orderking.domain.order.controller;

import com.sparta.orderking.config.Auth;
import com.sparta.orderking.domain.auth.dto.AuthUser;
import com.sparta.orderking.domain.order.dto.OrderResponseDto;
import com.sparta.orderking.domain.order.dto.UpdateOrderStatusRequestDto;
import com.sparta.orderking.domain.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;

    // 주문 생성
    @PostMapping("/{storeId}")
    public ResponseEntity<String> createOrder(@Auth AuthUser authUser, @PathVariable Long storeId) {
        orderService.createOrder(authUser.getUserId(), storeId);
        return ResponseEntity.ok("order success");
    }

    // 주문 상태 변경
    @PutMapping("/store/{storeId}/order/{orderId}")
    public ResponseEntity<String> updateOrderStatus(@Auth AuthUser authUser, @PathVariable Long storeId, @PathVariable Long orderId, @RequestBody UpdateOrderStatusRequestDto requestDto) {
        orderService.updateOrderStatus(authUser.getUserId(), storeId, orderId, requestDto);
        return ResponseEntity.ok("change order status success");
    }

    // 주문 조회
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponseDto> getOrder(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.getOrder(orderId));
    }
}