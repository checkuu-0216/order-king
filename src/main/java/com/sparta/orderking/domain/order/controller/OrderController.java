package com.sparta.orderking.domain.order.controller;

import com.sparta.orderking.domain.order.dto.CreateOrderRequestDto;
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
    public ResponseEntity<String> createOrder(@PathVariable Long storeId, @RequestBody CreateOrderRequestDto requestDto) {
        orderService.createOrder(1L, storeId, requestDto);
        return ResponseEntity.ok("주문이 완료되었습니다.");
    }

    // 주문 상태 변경
    @PutMapping("/store/{storeId}/order/{orderId}")
    public ResponseEntity<String> updateOrderStatus(@PathVariable Long storeId, @PathVariable Long orderId, @RequestBody UpdateOrderStatusRequestDto requestDto) {
        orderService.updateOrderStatus(storeId, orderId, requestDto);
        return ResponseEntity.ok("주문 상태 변경이 완료되었습니다.");
    }

    // 주문 조회
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponseDto> getOrder(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.getOrder(orderId));
    }
}