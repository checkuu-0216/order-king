package com.sparta.orderking.domain.cart.controller;

import com.sparta.orderking.config.Auth;
import com.sparta.orderking.domain.auth.dto.AuthUser;
import com.sparta.orderking.domain.cart.dto.CartRequestDto;
import com.sparta.orderking.domain.cart.dto.CartResponseDto;
import com.sparta.orderking.domain.cart.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/carts")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @PostMapping("/store/{storeId}/add/menu")
    public ResponseEntity<CartResponseDto> addMenu(@Auth AuthUser authUser, @PathVariable Long storeId, @RequestBody CartRequestDto requestDto) {
        return ResponseEntity.ok(cartService.addMenu(authUser.getUserId(), storeId, requestDto));
    }
}
