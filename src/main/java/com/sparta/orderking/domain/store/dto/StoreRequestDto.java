package com.sparta.orderking.domain.store.dto;

import com.sparta.orderking.domain.store.entity.StoreStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StoreRequestDto {
    private String name;
    private String storeAddress;
    private String storeNumber;
    private int minPrice;
    private LocalTime openTime;
    private LocalTime closeTime;
    private StoreStatus storeStatus;
}
