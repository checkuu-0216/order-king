package com.sparta.orderking.domain.store.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class StoreCheckResponseDto {

    private Long storeId;
    private String date;
    private Long Customers;
    private BigDecimal Sales;

}
