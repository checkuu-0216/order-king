package com.sparta.orderking.domain.store.entity;

import com.sparta.orderking.domain.store.dto.StoreRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Store {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "store_address")
    private String storeAddress;

    @Column(name = "store_number")
    private String storeNumber;

    @Column(name = "min_price")
    private int minPrice;

    @Column(name = "open_time")
    private int openTime;

    @Column(name = "close_time")
    private int closeTime;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private StoreServiceEnum service;

    public Store(StoreRequestDto storeRequestDto) {
        this.name = storeRequestDto.getName();
        this.storeAddress = storeRequestDto.getStoreAddress();
        this.storeNumber = storeRequestDto.getStoreNumber();
        this.minPrice = storeRequestDto.getMinPrice();
        this.openTime = storeRequestDto.getOpenTime();
        this.closeTime = storeRequestDto.getCloseTime();
        this.service = storeRequestDto.getStoreServiceEnum();
    }

    public void update(StoreRequestDto storeRequestDto) {
        this.name=storeRequestDto.getName();
        this.storeAddress = storeRequestDto.getStoreAddress();
        this.storeNumber = storeRequestDto.getStoreNumber();
        this.minPrice = storeRequestDto.getMinPrice();
        this.openTime = storeRequestDto.getOpenTime();
        this.closeTime = storeRequestDto.getCloseTime();
    }

    public void close() {
        this.service=StoreServiceEnum.CLOSED;
    }
}
