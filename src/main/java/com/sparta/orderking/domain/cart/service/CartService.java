package com.sparta.orderking.domain.cart.service;

import com.sparta.orderking.domain.cart.dto.CartRequestDto;
import com.sparta.orderking.domain.cart.dto.CartResponseDto;
import com.sparta.orderking.domain.cart.entity.Cart;
import com.sparta.orderking.domain.cart.repository.CartRepository;
import com.sparta.orderking.domain.menu.entity.Menu;
import com.sparta.orderking.domain.menu.repository.MenuRepository;
import com.sparta.orderking.domain.store.entity.Store;
import com.sparta.orderking.domain.store.repository.StoreRepository;
import com.sparta.orderking.domain.user.entity.User;
import com.sparta.orderking.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final StoreRepository storeRepository;
    private final MenuRepository menuRepository;

    @Transactional
    public CartResponseDto addMenu(Long userId, Long storeId, CartRequestDto requestDto) {
        User user = userRepository.findById(userId).orElseThrow();
        Store store = storeRepository.findById(storeId).orElseThrow();

        Cart cart = cartRepository.findByUser(user); // findByUser
        LocalDateTime now = LocalDateTime.now();

        if(cart != null) {
            // 가게 변경 시 or 카트 24시간 후 장바구니 초기화
            // 스케쥴러 카트 삭제
            if (!cart.getStore().getId().equals(storeId) || now.isAfter(cart.getLastUpdated().plusHours(24))) {
                cart.clear();
            }
        } else {
            cart = new Cart(user, store);
        }

        for (Long menuId : requestDto.getMenuList()) {
            // in => 받아와서 비교
            Menu menu = menuRepository.findById(menuId).orElseThrow(() -> new IllegalArgumentException("Menu not found"));

            cart.addMenu(menu);
        }

        cartRepository.save(cart);

        return new CartResponseDto(cart);
    }


    @Transactional()
    public CartResponseDto getCart(Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        Cart cart = cartRepository.findByUser(user);

        LocalDateTime now = LocalDateTime.now();

        //스케쥴러
        if (now.isAfter(cart.getLastUpdated().plusHours(24))) {
            cart.clear();
        }

        return new CartResponseDto(cart);
    }

    @Transactional
    public CartResponseDto clearCart(Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        Cart cart = cartRepository.findByUser(user);

        cart.clear();

        return new CartResponseDto(cart);
    }

    @Transactional
    public CartResponseDto removeMenu(Long userId, Long menuId) {
        User user = userRepository.findById(userId).orElseThrow();
        Cart cart = cartRepository.findByUser(user); //optional

        Menu menu = menuRepository.findById(menuId).orElseThrow();

        cart.removeMenu(menu);

        return new CartResponseDto(cart);
    }
}
