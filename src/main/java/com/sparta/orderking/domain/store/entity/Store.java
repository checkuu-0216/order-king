package com.sparta.orderking.domain.store.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sparta.orderking.common.BaseEntity;
import com.sparta.orderking.domain.store.dto.StoreRequestDto;
import com.sparta.orderking.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Store extends BaseEntity {
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user")
    private User user;

    @Column(name = "open_time")
    @JsonFormat(pattern = "HH:mm", timezone = "Asia/Seoul")
    private LocalTime openTime;

    @Column(name = "close_time")
    @JsonFormat(pattern = "HH:mm", timezone = "Asia/Seoul")
    private LocalTime closeTime;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private StoreStatus storeStatus;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private StoreAdEnum storeAdEnum;

    public Store(StoreRequestDto storeRequestDto,User user) {
        this.name = storeRequestDto.getName();
        this.storeAddress = storeRequestDto.getStoreAddress();
        this.storeNumber = storeRequestDto.getStoreNumber();
        this.minPrice = storeRequestDto.getMinPrice();
        this.openTime = storeRequestDto.getOpenTime();
        this.closeTime = storeRequestDto.getCloseTime();
        this.storeStatus = storeRequestDto.getStoreStatus();
        this.storeAdEnum = storeRequestDto.getStoreAdEnum();
        this.user = user;
    }

    public void update(StoreRequestDto storeRequestDto) {
        this.name=storeRequestDto.getName();
        this.storeAddress = storeRequestDto.getStoreAddress();
        this.storeNumber = storeRequestDto.getStoreNumber();
        this.minPrice = storeRequestDto.getMinPrice();
        this.openTime = storeRequestDto.getOpenTime();
        this.closeTime = storeRequestDto.getCloseTime();
        this.storeStatus = storeRequestDto.getStoreStatus();
        this.storeAdEnum = storeRequestDto.getStoreAdEnum();
    }

    public void close() {
        this.storeStatus = StoreStatus.CLOSED;
    }

    public void turnOnAd() {
        this.storeAdEnum=StoreAdEnum.ON;
    }
    public void turnOffAd() {
        this.storeAdEnum=StoreAdEnum.OFF;
    }

}
