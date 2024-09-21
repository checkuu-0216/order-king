package com.sparta.orderking.domain.store.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StoreCheckMonthlyResponseDto {
    private Long storeId;
    private String month; // 월 추가
    private Long monthlyCustomers;
    private Long monthlySales;
}