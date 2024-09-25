package com.sparta.orderking.domain.order.enums;

import com.sun.jdi.request.InvalidRequestStateException;

import java.util.Arrays;

public enum OrderStatus {
    PENDING(1), // 주문 접수
    PREPARING(2), // 준비중
    IN_DELIVERY(3), // 배송중
    DELIVERY_COMPLETED(4), // 배송 완료
    WITHDRAW(5); // 주문 취소

    private final int value;

    OrderStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

    public static OrderStatus of(String status) {
        return Arrays.stream(OrderStatus.values())
                .filter(s -> s.name().equalsIgnoreCase(status))
                .findFirst()
                .orElseThrow(() -> new InvalidRequestStateException("유효하지 않은 OrderStatus"));
    }
}