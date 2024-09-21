package com.sparta.orderking.domain.store.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StoreCheckDailyResponseDto {

    private Long storeId;
    private String date;
    private Long dailyCustomers;
    private Long dailySales;

}
