package com.sparta.orderking.domain.store.dto.response;

import com.sparta.orderking.domain.store.entity.Store;
import com.sparta.orderking.domain.store.entity.StoreAdEnum;
import com.sparta.orderking.domain.store.entity.StoreStatus;
import lombok.Getter;

import java.time.LocalTime;

@Getter
public class StoreNotificationResponseDto {
    private final Long id;
    private final String name;
    private final String storeAddress;
    private final String storeNumber;
    private final Integer minPrice;
    private final LocalTime openTime;
    private final LocalTime closeTime;
    private final StoreStatus storeStatus;
    private final StoreAdEnum storeAdEnum;
    private final String notification;

    public StoreNotificationResponseDto(Store store) {
        this.id = store.getId();
        this.name = store.getName();
        this.storeAddress = store.getStoreAddress();
        this.storeNumber = store.getStoreNumber();
        this.minPrice = store.getMinPrice();
        this.openTime = store.getOpenTime();
        this.closeTime = store.getCloseTime();
        this.storeStatus = store.getStoreStatus();
        this.storeAdEnum = store.getStoreAdEnum();
        this.notification = store.getNotification();
    }
}
