package com.sparta.orderking.domain.order.enums;

import com.sun.jdi.request.InvalidRequestStateException;

import java.util.Arrays;

public enum OrderStatus {
    PENDING, // 주문 접수
    PREPARING, // 준비중
    IN_DELIVERY, // 배송중
    DELIVERY_COMPLETED, // 배송 완료
    WITHDRAW; // 주문 취소

    public static OrderStatus of(String status) {
        return Arrays.stream(OrderStatus.values())
                .filter(s -> s.name().equalsIgnoreCase(status))
                .findFirst()
                .orElseThrow(() -> new InvalidRequestStateException("유효하지 않은 OrderStatus"));
    }
}