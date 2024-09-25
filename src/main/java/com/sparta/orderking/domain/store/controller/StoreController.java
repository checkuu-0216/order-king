package com.sparta.orderking.domain.store.controller;

import com.sparta.orderking.config.Auth;
import com.sparta.orderking.domain.auth.dto.AuthUser;
import com.sparta.orderking.domain.store.dto.request.StoreNotificationRequestDto;
import com.sparta.orderking.domain.store.dto.request.StoreRequestDto;
import com.sparta.orderking.domain.store.dto.request.StoreSimpleRequestDto;
import com.sparta.orderking.domain.store.dto.response.*;
import com.sparta.orderking.domain.store.service.StoreService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class StoreController {

    private final StoreService storeService;

    @PostMapping("/stores")
    public ResponseEntity<StoreResponseDto> saveStore(@Auth AuthUser authUser, @RequestBody @Valid StoreRequestDto storeRequestDto) {
        return ResponseEntity.ok(storeService.saveStore(authUser, storeRequestDto));
    }

    @PutMapping("/stores/{storeId}")
    public ResponseEntity<StoreResponseDto> updateStore(@Auth AuthUser authUser
            , @PathVariable long storeId, @RequestBody @Valid StoreRequestDto storeRequestDto) {
        return ResponseEntity.ok(storeService.updateStore(authUser, storeId, storeRequestDto));
    }

    @GetMapping("/stores/{storeId}")
    public ResponseEntity<StoreDetailResponseDto> getDetailStore(@PathVariable long storeId) {
        return ResponseEntity.ok(storeService.getDetailStore(storeId));
    }

    @GetMapping("/stores")
    public ResponseEntity<List<StoreResponseDto>> getStore(@RequestBody @Valid StoreSimpleRequestDto storeSimpleRequestDto) {
        return ResponseEntity.ok(storeService.getStore(storeSimpleRequestDto));
    }

    @PutMapping("/stores/{storeId}/close")
    public ResponseEntity<String> closeStore(@Auth AuthUser authUser, @PathVariable long storeId) {
        storeService.closeStore(authUser, storeId);
        return ResponseEntity.ok().body("store is closed successfully");
    }

    @PutMapping("/stores/{storeId}/ad")
    public ResponseEntity<String> storeAd(@Auth AuthUser authUser, @PathVariable long storeId) {
        storeService.storeAd(authUser, storeId);
        return ResponseEntity.ok().body("ad condition changed successfully");
    }

    @GetMapping("/stores/mystore")
    public ResponseEntity<List<StoreCheckResponseDto>> checkDailyMyStore(@Auth AuthUser authUser,
                                                                         @RequestParam String type,
                                                                         @RequestParam LocalDate startDate,
                                                                         @RequestParam LocalDate endDate) {
        return ResponseEntity.ok(storeService.checkMyStore(authUser,type,startDate,endDate));
    }

    @PutMapping("/stores/{storeId}/notification")
    public ResponseEntity<StoreNotificationResponseDto> changeNotification(@Auth AuthUser authUser, @PathVariable long storeId,
                                                                           @RequestBody StoreNotificationRequestDto storeNotificationRequestDto) {
        return ResponseEntity.ok(storeService.changeNotification(authUser, storeId, storeNotificationRequestDto));
    }

}
