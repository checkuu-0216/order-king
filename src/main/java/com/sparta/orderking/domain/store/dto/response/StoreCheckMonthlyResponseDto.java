package com.sparta.orderking.domain.store.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StoreCheckMonthlyResponseDto {
    private Long storeId;
    private String month;
    private Long monthlyCustomers;
    private Long monthlySales;
}