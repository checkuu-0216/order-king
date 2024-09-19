package com.sparta.orderking.domain.store.dto;

import com.sparta.orderking.domain.store.entity.Store;
import com.sparta.orderking.domain.menu.entity.Menu;
import lombok.Getter;

import java.time.LocalTime;
import java.util.List;

@Getter
public class StoreDetailResponseDto {
    private final Long id;
    private final String name;
    private final String storeAddress;
    private final String storeNumber;
    private final int minPrice;
    private final LocalTime openTime;
    private final LocalTime closeTime;
    private final List<Menu> menuList;

    public StoreDetailResponseDto(Store store, List<Menu> menuList) {
        this.id = store.getId();
        this.name = store.getName();
        this.storeAddress = store.getStoreAddress();
        this.storeNumber = store.getStoreNumber();
        this.minPrice = store.getMinPrice();
        this.openTime = store.getOpenTime();
        this.closeTime = store.getCloseTime();
        this.menuList = menuList;
    }
}
