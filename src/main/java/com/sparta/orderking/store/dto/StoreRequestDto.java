package com.sparta.orderking.store.dto;

import com.sparta.orderking.store.entity.StoreServiceEnum;
import lombok.Getter;

@Getter
public class StoreRequestDto {
    private String name;
    private String storeAddress;
    private String storeNumber;
    private int minPrice;
    private int openTime;
    private int closeTime;
    private StoreServiceEnum storeServiceEnum;

}
