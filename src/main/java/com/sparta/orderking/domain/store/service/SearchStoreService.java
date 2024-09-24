package com.sparta.orderking.domain.store.service;

import com.sparta.orderking.domain.menu.entity.MenuCategoryEnum;
import com.sparta.orderking.domain.menu.repository.MenuRepository;
import com.sparta.orderking.domain.store.dto.response.StoreCategoryResponseDto;
import com.sparta.orderking.domain.store.dto.response.StoreSimpleResponseDto;
import com.sparta.orderking.domain.store.entity.Store;
import com.sparta.orderking.domain.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchStoreService {
    private final StoreRepository storeRepository;
    private final MenuRepository menuRepository;

    //이름으로 가게 찾기
    public List<StoreSimpleResponseDto> searchStoreByNameOrMenu(String keyword) {
        //keyword 가 포함된 가게 및 메뉴 찾기
        List<Store> storeByNameAndMenu = storeRepository.findStoresByStoreNameOrMenuName(keyword);

        //찾은 가게를 리스트화
        List<StoreSimpleResponseDto> result = new ArrayList<>();
        for (Store store : storeByNameAndMenu) {
            StoreSimpleResponseDto dto = new StoreSimpleResponseDto(store.getName());
            result.add(dto);
        }

        return result;
    }

    public List<StoreCategoryResponseDto> searchStoresByCategory(MenuCategoryEnum menuCategoryEnum) {
        //카테고리 이넘이 포함된 가게 찾기
        List<Store> store = storeRepository.findStoresByMenuCategory(menuCategoryEnum);
        //찾은 가게 리스트 하기
        List<StoreCategoryResponseDto> storeList = new ArrayList<>();
        for (Store store1 : store) {
            StoreCategoryResponseDto dto = new StoreCategoryResponseDto(store1.getName());
            storeList.add(dto);
        }
        return storeList;
    }
}