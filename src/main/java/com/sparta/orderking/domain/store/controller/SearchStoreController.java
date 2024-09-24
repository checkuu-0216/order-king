package com.sparta.orderking.domain.store.controller;

import com.sparta.orderking.domain.menu.entity.MenuCategoryEnum;
import com.sparta.orderking.domain.store.dto.response.StoreCategoryResponseDto;
import com.sparta.orderking.domain.store.dto.response.StoreSimpleResponseDto;
import com.sparta.orderking.domain.store.service.SearchStoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class SearchStoreController {

    private final SearchStoreService searchStoreService;

    @GetMapping("/stores/search")
    public ResponseEntity<List<StoreSimpleResponseDto>> searchStores(@RequestParam String keyword){
        //이름으로 찾고 리스트 리턴
        List<StoreSimpleResponseDto> storeList = searchStoreService.searchStoreByNameOrMenu(keyword);
        return ResponseEntity.ok(storeList);
    }

    @GetMapping("/stores/category")
    public ResponseEntity<List<StoreCategoryResponseDto>> searchCategory(@RequestParam MenuCategoryEnum menuCategoryEnum){
        return ResponseEntity.ok(searchStoreService.searchStoresByCategory(menuCategoryEnum));
    }
}
