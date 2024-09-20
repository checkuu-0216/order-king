package com.sparta.orderking.domain.store.service;

import com.sparta.orderking.config.AuthUser;
import com.sparta.orderking.domain.menu.entity.Menu;
import com.sparta.orderking.domain.menu.repository.MenuRepository;
import com.sparta.orderking.domain.store.dto.StoreDetailResponseDto;
import com.sparta.orderking.domain.store.dto.StoreRequestDto;
import com.sparta.orderking.domain.store.dto.StoreResponseDto;
import com.sparta.orderking.domain.store.dto.StoreSimpleRequestDto;
import com.sparta.orderking.domain.store.entity.Store;
import com.sparta.orderking.domain.store.entity.StoreStatus;
import com.sparta.orderking.domain.store.repository.StoreRepository;
import com.sparta.orderking.domain.user.entity.UserEnum;
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
        return store.getStoreStatus().equals(StoreStatus.OPEN);
    }

    public void checkAdmin(AuthUser authUser){
        if(!authUser.getUserEnum().equals(UserEnum.OWNER)){
            throw new RuntimeException("owner only");
        }
    }

    @Transactional
    public StoreResponseDto saveStore(AuthUser authUser, StoreRequestDto storeRequestDto) {
        checkAdmin(authUser);
        Store store = new Store(storeRequestDto);
        Store savedstore = storeRepository.save(store);
        return new StoreResponseDto(savedstore);
    }

    @Transactional
    public StoreResponseDto updateStore(AuthUser authUser, Long storeId, StoreRequestDto storeRequestDto) {
        checkAdmin(authUser);
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
        List<Store> storeList = storeRepository.findByNameAndStoreStatus(storeSimpleRequestDto.getName(), StoreStatus.OPEN);
        List<StoreResponseDto> dtoList = new ArrayList<>();
        for (Store store : storeList) {
            StoreResponseDto dto = new StoreResponseDto(store);
            dtoList.add(dto);
        }
        return dtoList;
    }
    @Transactional
    public void closeStore(AuthUser authUser, Long storeId) {
        checkAdmin(authUser);
        Store store = storeRepository.findById(storeId).orElseThrow(()->new NullPointerException("no such store"));
        if(store.getStoreStatus().equals(StoreStatus.CLOSED)){
            throw new RuntimeException("already closed");
        }
        store.close();
    }
}
