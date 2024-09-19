package com.sparta.orderking.domain.store.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sparta.orderking.domain.store.dto.StoreRequestDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
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
    @JsonFormat(pattern = "HH:mm", timezone = "Asia/Seoul")
    private LocalTime openTime;

    @Column(name = "close_time")
    @JsonFormat(pattern = "HH:mm", timezone = "Asia/Seoul")
    private LocalTime closeTime;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private StoreStatus storeStatus;

    public Store(StoreRequestDto storeRequestDto) {
        this.name = storeRequestDto.getName();
        this.storeAddress = storeRequestDto.getStoreAddress();
        this.storeNumber = storeRequestDto.getStoreNumber();
        this.minPrice = storeRequestDto.getMinPrice();
        this.openTime = storeRequestDto.getOpenTime();
        this.closeTime = storeRequestDto.getCloseTime();
        this.storeStatus = storeRequestDto.getStoreStatus();
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
        this.storeStatus = StoreStatus.CLOSED;
    }
}
