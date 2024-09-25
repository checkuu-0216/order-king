package com.sparta.orderking.store.service;

import com.sparta.orderking.domain.menu.entity.MenuCategoryEnum;
import com.sparta.orderking.domain.store.dto.response.StoreCategoryResponseDto;
import com.sparta.orderking.domain.store.dto.response.StoreSimpleResponseDto;
import com.sparta.orderking.domain.store.entity.Store;
import com.sparta.orderking.domain.store.repository.StoreRepository;
import com.sparta.orderking.domain.store.service.SearchStoreService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class SearchStoreServiceTest {
    @Mock
    private StoreRepository storeRepository;
    @InjectMocks
    private SearchStoreService searchStoreService;

    @Test
    void searchStoreByNameOrMenu() {
        String keyword = "1";
        List<Store> storeList = new ArrayList<>();
        given(storeRepository.findStoresByStoreNameOrMenuName(anyString())).willReturn(storeList);

        List<StoreSimpleResponseDto> result = searchStoreService.searchStoreByNameOrMenu(keyword);

        assertNotNull(result);
    }

    @Test
    void searchStoresByCategory() {
        MenuCategoryEnum menuCategoryEnum = MenuCategoryEnum.CHICKEN;
        List<Store> storeList = new ArrayList<>();
        given(storeRepository.findStoresByMenuCategory(any())).willReturn(storeList);

        List<StoreCategoryResponseDto> responseDtos = searchStoreService.searchStoresByCategory(menuCategoryEnum);

        assertNotNull(responseDtos);
    }
}
