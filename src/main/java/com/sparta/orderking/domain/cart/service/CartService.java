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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final StoreRepository storeRepository;
    private final MenuRepository menuRepository;

    public CartResponseDto addMenu(Long userId, Long storeId, CartRequestDto requestDto) {
        //- 한 가게의 메뉴만 담을 수 있으며, 가게가 변경될 시 장바구니는 초기화 됩니다.
        //- 장바구니는 마지막 업데이트 시점으로부터 최대 하루만 유지 됩니다.
        User user = userRepository.findById(userId).orElseThrow();
        Store store = storeRepository.findById(storeId).orElseThrow();

        Cart cart = cartRepository.findByUser(user);

        if(cart != null) {
            if (!cart.getStore().getId().equals(storeId)) {
                cart.clear(); // 가게 변경 시 장바구니 초기화
            }
        } else {
            cart = new Cart(user, store);
        }

        for (Long menuId : requestDto.getMenuList()) {
            Menu menu = menuRepository.findById(menuId).orElseThrow(() -> new IllegalArgumentException("Menu not found"));
            cart.addMenu(menu); // 장바구니에 메뉴 항목 추가
        }

        cartRepository.save(cart);

        return new CartResponseDto(cart);
    }
}
