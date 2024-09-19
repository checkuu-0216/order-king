package com.sparta.orderking.store.service;

import com.sparta.orderking.store.delete.AuthUser;
import com.sparta.orderking.store.delete.Menu;
import com.sparta.orderking.store.delete.MenuRepository;
import com.sparta.orderking.store.delete.UserRoleEnum;
import com.sparta.orderking.store.dto.StoreDetailResponseDto;
import com.sparta.orderking.store.dto.StoreRequestDto;
import com.sparta.orderking.store.dto.StoreResponseDto;
import com.sparta.orderking.store.dto.StoreSimpleRequestDto;
import com.sparta.orderking.store.entity.Store;
import com.sparta.orderking.store.entity.StoreServiceEnum;
import com.sparta.orderking.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StoreService {

    private final StoreRepository storeRepository;
    private final MenuRepository menuRepository;

    public Store findStoreById(Long storeId){
        return storeRepository.findById(storeId).orElseThrow(()->new NullPointerException("there is no Store"));
    }

    public Boolean storeIsOpen(Store store){
        return store.getService().equals(StoreServiceEnum.OPEN);
    }

    @Transactional
    public StoreResponseDto saveStore(AuthUser authUser, StoreRequestDto storeRequestDto) {
        if(!authUser.getUserEnum().equals(UserRoleEnum.OWNER)){
            throw new RuntimeException("owner only");
        }
        Store store = new Store(storeRequestDto);
        Store savedstore = storeRepository.save(store);
        return new StoreResponseDto(savedstore);
    }

    @Transactional
    public StoreResponseDto updateStore(AuthUser authUser, Long storeId, StoreRequestDto storeRequestDto) {
        if(!authUser.getUserEnum().equals(UserRoleEnum.OWNER)){
            throw new RuntimeException("owner only");
        }
        Store store = findStoreById(storeId);
        store.update(storeRequestDto);
        return new StoreResponseDto(store);
    }

    public StoreDetailResponseDto getDetailStore(Long storeId) {
        Store store = findStoreById(storeId);
        if(!storeIsOpen(store)){
            throw new RuntimeException("store is closed");
        }
        List<Menu> menuList = menuRepository.findMenuByStoreId(storeId);
        return new StoreDetailResponseDto(store,menuList);
    }

    public List<StoreResponseDto> getStore(StoreSimpleRequestDto storeSimpleRequestDto) {
        List<Store> storeList = storeRepository.findByName(storeSimpleRequestDto.getName());
        List<StoreResponseDto> dtoList = new ArrayList<>();
        for (Store store : storeList) {
            StoreResponseDto dto = new StoreResponseDto(store);
            dtoList.add(dto);
        }
        return dtoList;
    }
}
