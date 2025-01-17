package com.sparta.orderking.domain.store.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sparta.orderking.common.BaseEntity;
import com.sparta.orderking.domain.menu.entity.Menu;
import com.sparta.orderking.domain.store.dto.request.StoreRequestDto;
import com.sparta.orderking.domain.user.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Store extends BaseEntity {

    @Column(name = "name")
    private String name;

    @Column(name = "store_address")
    private String storeAddress;

    @Column(name = "store_number")
    private String storeNumber;

    @Column(name = "min_price")
    private Integer minPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "open_time")
    @JsonFormat(pattern = "HH:mm")
    private LocalTime openTime;

    @Column(name = "close_time")
    @JsonFormat(pattern = "HH:mm")
    private LocalTime closeTime;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private StoreStatus storeStatus;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private StoreAdEnum storeAdEnum;

    @Size(max = 255)
    @Column(name = "notification")
    private String notification;

    @OneToMany(mappedBy = "store", fetch = FetchType.LAZY)
    private List<Menu> menus;

    public Store(StoreRequestDto storeRequestDto, User user) {
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

    public Store(long l, String name, String storeAddress, String storeNumber, int i, User testUser, LocalTime of, LocalTime of1, StoreStatus storeStatus, StoreAdEnum storeAdEnum) {
        this.id = l;
        this.name = name;
        this.storeAddress = storeAddress;
        this.storeNumber = storeNumber;
        this.minPrice = i;
        this.user = testUser;
        this.openTime = of;
        this.closeTime = of1;
        this.storeStatus = storeStatus;
        this.storeAdEnum = storeAdEnum;
    }

    public Store(long l, String name, String storeAddress, String storeNumber, int i, User testUser, LocalTime of, LocalTime of1, StoreStatus storeStatus, StoreAdEnum storeAdEnum, String notification) {
        this.id = l;
        this.name = name;
        this.storeAddress = storeAddress;
        this.storeNumber = storeNumber;
        this.minPrice = i;
        this.user = testUser;
        this.openTime = of;
        this.closeTime = of1;
        this.storeStatus = storeStatus;
        this.storeAdEnum = storeAdEnum;
        this.notification = notification;
    }

    public void update(StoreRequestDto storeRequestDto) {
        this.name = storeRequestDto.getName();
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
        this.storeAdEnum = StoreAdEnum.ON;
    }

    public void turnOffAd() {
        this.storeAdEnum = StoreAdEnum.OFF;
    }

    public void updateNotification(String notification) {
        this.notification = notification;
    }
}
