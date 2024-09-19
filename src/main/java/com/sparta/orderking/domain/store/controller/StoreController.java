package com.sparta.orderking.domain.store.controller;

import com.sparta.orderking.config.Auth;
import com.sparta.orderking.config.AuthUser;
import com.sparta.orderking.domain.store.dto.StoreDetailResponseDto;
import com.sparta.orderking.domain.store.dto.StoreRequestDto;
import com.sparta.orderking.domain.store.dto.StoreResponseDto;
import com.sparta.orderking.domain.store.dto.StoreSimpleRequestDto;
import com.sparta.orderking.domain.store.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class StoreController {

    private final StoreService storeService;

    @PostMapping("/stores")
    public ResponseEntity<StoreResponseDto> saveStore(@Auth AuthUser authUser, @RequestBody StoreRequestDto storeRequestDto){
        return ResponseEntity.ok(storeService.saveStore(authUser,storeRequestDto));
    }

    @PutMapping("/stores/{storeId}")
    public ResponseEntity<StoreResponseDto> updateStore(@Auth AuthUser authUser
            , @PathVariable Long storeId,@RequestBody StoreRequestDto storeRequestDto){
        return ResponseEntity.ok(storeService.updateStore(authUser,storeId,storeRequestDto));
    }

    @GetMapping("/stores/{storeId}")
    public ResponseEntity<StoreDetailResponseDto> getDetailStore(@PathVariable Long storeId){
        return ResponseEntity.ok(storeService.getDetailStore(storeId));
    }

    @GetMapping("/stores")
    public ResponseEntity<List<StoreResponseDto>> getStore(@RequestBody StoreSimpleRequestDto storeSimpleRequestDto){
        return ResponseEntity.ok(storeService.getStore(storeSimpleRequestDto));
    }

    @PutMapping("/stores/{storeId}/close")
    public void closeStore(@Auth AuthUser authUser,@PathVariable Long storeId){
        storeService.closeStore(authUser,storeId);
    }

}
