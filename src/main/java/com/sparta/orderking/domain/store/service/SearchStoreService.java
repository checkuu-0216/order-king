package com.sparta.orderking.domain.store.service;

import com.sparta.orderking.domain.menu.dto.MenuResponseDto;
import com.sparta.orderking.domain.menu.dto.MenuSimpleResponseDto;
import com.sparta.orderking.domain.menu.entity.Menu;
import com.sparta.orderking.domain.menu.repository.MenuRepository;
import com.sparta.orderking.domain.store.dto.response.StoreResponseDto;
import com.sparta.orderking.domain.store.dto.response.StoreSimpleResponseDto;
import com.sparta.orderking.domain.store.entity.Store;
import com.sparta.orderking.domain.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class SearchStoreService {
    private final StoreRepository storeRepository;
    private final MenuRepository menuRepository;

    //이름으로 가게 찾기
    public List<StoreSimpleResponseDto> searchStoreByNameOrMenu(String keyword) {
        List<Store> storeByName = storeRepository.findByNameContaining(keyword);
        List<Store> storeByMenu = storeRepository.findStoreByMenuName(keyword);

        Map<String, StoreSimpleResponseDto> resultMap = new HashMap<>();

        for (Store store : storeByName) {
            resultMap.put(store.getName(), new StoreSimpleResponseDto(store.getName(), new ArrayList<>()));
        }

        // 메뉴로 추가
        for (Store menuStore : storeByMenu) {
            for (Menu menu : menuStore.getMenus()) { // 가게의 모든 메뉴를 순회
                resultMap.computeIfPresent(menuStore.getName(), (key, dto) -> {
                    dto.getMenus().add(menu.getMenuName()); // 실제 메뉴 이름을 추가
                    return dto;
                });
            }
        }
        return new ArrayList<>(resultMap.values());
    }
}
    //        //키워드 포함된 가게 저장하기
//        Set<StoreSimpleResponseDto> result = new HashSet<>();
//        for (Store store : storeByName) {
//            StoreSimpleResponseDto dto = new StoreSimpleResponseDto(store.getName());
//            result.add(dto);
//        }
    //키워드를 포함한 메뉴를 가진 가게 저장하기
//        Set<StoreSimpleResponseDto> storeNameByMenu = new HashSet<>();
//        for (Store menu : storeByMenu) {
//            StoreSimpleResponseDto dto = new StoreSimpleResponseDto(menu.getName());
//            storeNameByMenu.add(dto);
//        }
    //검색된 결과 병합
//        result.addAll(storeNameByMenu);
//        return new ArrayList<>(result);

