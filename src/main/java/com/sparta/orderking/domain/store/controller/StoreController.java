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
    public void closeStore(@Auth AuthUser authUser, @PathVariable long storeId) {
        storeService.closeStore(authUser, storeId);
    }

    @PutMapping("/stores/{storeId}/adon")
    public void storeAdOn(@Auth AuthUser authUser, @PathVariable long storeId) {
        storeService.storeAdOn(authUser, storeId);
    }

    @PutMapping("/stores/{storeId}/adoff")
    public void storeAdOff(@Auth AuthUser authUser, @PathVariable long storeId) {
        storeService.storeAdOff(authUser, storeId);
    }
//안댐
    @GetMapping("/stores/checkdaily")
    public ResponseEntity<List<StoreCheckDailyResponseDto>> checkDailyMyStore(@Auth AuthUser authUser) {
        return ResponseEntity.ok(storeService.checkDailyMyStore(authUser));
    }
//안댐
    @GetMapping("/stores/checkmonthly")
    public ResponseEntity<List<StoreCheckMonthlyResponseDto>> checkMonthlyMyStore(@Auth AuthUser authUser) {
        return ResponseEntity.ok(storeService.checkMonthlyMyStore(authUser));
    }

    @PutMapping("/stores/{storeId}/notification")
    public ResponseEntity<StoreNotificationResponseDto> changeNotification(@Auth AuthUser authUser, @PathVariable long storeId,
                                                                           @RequestBody StoreNotificationRequestDto storeNotificationRequestDto) {
        return ResponseEntity.ok(storeService.changeNotification(authUser, storeId, storeNotificationRequestDto));
    }

}
