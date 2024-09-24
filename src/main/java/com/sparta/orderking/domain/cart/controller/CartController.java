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

    @GetMapping("/get/cart")
    public ResponseEntity<CartResponseDto> getCart(@Auth AuthUser authUser) {
        return ResponseEntity.ok(cartService.getCart(authUser.getUserId()));
    }

    @GetMapping("/clear")
    public ResponseEntity<CartResponseDto> clearCart(@Auth AuthUser authUser) {
        return ResponseEntity.ok(cartService.clearCart(authUser.getUserId()));
    }

    @GetMapping("/remove/menu/{menuId}")
    public ResponseEntity<CartResponseDto> removeMenu(@Auth AuthUser authUser, @PathVariable Long menuId) {
        return ResponseEntity.ok(cartService.removeMenu(authUser.getUserId(), menuId));
    }
}
