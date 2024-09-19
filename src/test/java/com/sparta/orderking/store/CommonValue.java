package com.sparta.orderking.store;

import com.sparta.orderking.config.AuthUser;
import com.sparta.orderking.domain.store.dto.StoreRequestDto;
import com.sparta.orderking.domain.store.dto.StoreResponseDto;
import com.sparta.orderking.domain.store.entity.Store;
import com.sparta.orderking.domain.store.entity.StoreServiceEnum;

public class CommonValue {
    public final static StoreRequestDto TEST_STOREREQUESTDTO = new StoreRequestDto("name","storeAddress","storeNumber",10000,9,21,StoreServiceEnum.OPEN);
    public final static Store TEST_STORE = new Store(1L,"name","storeAddress","storeNumber",10000,9,21, StoreServiceEnum.OPEN);
    public final static Store TEST_STORE2 = new Store(1L,"name","storeAddress","storeNumber",10000,9,21, StoreServiceEnum.CLOSED);
    public final static StoreResponseDto TEST_STORERESPONSEDTO = new StoreResponseDto(TEST_STORE);
    public final static AuthUser TEST_AUTHUSER2 = new AuthUser(1L, UserRoleEnum.USER);
    public final static AuthUser TEST_AUTHUSER = new AuthUser(1L, UserRoleEnum.OWNER);
}
