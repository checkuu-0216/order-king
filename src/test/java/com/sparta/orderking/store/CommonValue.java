package com.sparta.orderking.store;

import com.sparta.orderking.config.AuthUser;
import com.sparta.orderking.domain.store.dto.request.StoreRequestDto;
import com.sparta.orderking.domain.store.dto.response.StoreResponseDto;
import com.sparta.orderking.domain.store.entity.Store;
import com.sparta.orderking.domain.store.entity.StoreAdEnum;
import com.sparta.orderking.domain.store.entity.StoreStatus;
import com.sparta.orderking.domain.user.entity.User;
import com.sparta.orderking.domain.user.entity.UserEnum;

import java.time.LocalTime;

public class CommonValue {
    public final static User TEST_USER = new User(UserEnum.OWNER);
    public final static StoreRequestDto TEST_STOREREQUESTDTO = new StoreRequestDto("name","storeAddress","storeNumber",10000,LocalTime.of(9,0),LocalTime.of(21,0), StoreStatus.OPEN, StoreAdEnum.ON);
    public final static Store TEST_STORE = new Store(1L,"name","storeAddress","storeNumber",10000, TEST_USER, LocalTime.of(9,0),LocalTime.of(21,0), StoreStatus.OPEN,StoreAdEnum.ON);
    public final static Store TEST_STORE2 = new Store(1L,"name","storeAddress","storeNumber",10000,TEST_USER,LocalTime.of(9,0),LocalTime.of(21,0), StoreStatus.CLOSED,StoreAdEnum.ON);
    public final static Store TEST_STORE3 = new Store(1L,"name","storeAddress","storeNumber",10000,TEST_USER,LocalTime.of(9,0),LocalTime.of(21,0), StoreStatus.CLOSED,StoreAdEnum.ON,"notification");
    public final static StoreResponseDto TEST_STORERESPONSEDTO = new StoreResponseDto(TEST_STORE);
    public final static AuthUser TEST_AUTHUSER2 = new AuthUser(1L, UserEnum.USER);
    public final static AuthUser TEST_AUTHUSER = new AuthUser(1L, UserEnum.OWNER);
}
