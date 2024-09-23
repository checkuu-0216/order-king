package com.sparta.orderking.domain.store.service;

import com.sparta.orderking.domain.menu.repository.MenuRepository;
import com.sparta.orderking.domain.store.dto.response.StoreResponseDto;
import com.sparta.orderking.domain.store.dto.response.StoreSimpleResponseDto;
import com.sparta.orderking.domain.store.entity.Store;
import com.sparta.orderking.domain.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class SearchStoreService {
    private final StoreRepository storeRepository;
    private final MenuRepository menuRepository;

    //이름으로 가게 찾기
    public List<StoreSimpleResponseDto> searchStoreByNameOrMenu(String keyword) {
        List<Store> storeByName = storeRepository.findByNameContaining(keyword);
        List<Store> storeByMenu = storeRepository.findStoreByMenuName(keyword);

        //키워드 포함된 가게 저장하기
        Set<StoreSimpleResponseDto> result = new HashSet<>();
        for (Store store : storeByName) {
            StoreSimpleResponseDto dto = new StoreSimpleResponseDto(store.getName());
            result.add(dto);
        }
        //키워드를 포함한 메뉴를 가진 가게 저장하기
        Set<StoreSimpleResponseDto> storeNameByMenu = new HashSet<>();
        for (Store menu : storeByMenu) {
            StoreSimpleResponseDto dto = new StoreSimpleResponseDto(menu.getName());
            storeNameByMenu.add(dto);
        }

        //검색된 결과 병합
        result.addAll(storeNameByMenu);
        return new ArrayList<>(result);
    }
}
