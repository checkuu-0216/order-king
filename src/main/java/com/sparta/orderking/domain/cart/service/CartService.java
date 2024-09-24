package com.sparta.orderking.domain.cart.service;

import com.sparta.orderking.domain.cart.dto.CartRequestDto;
import com.sparta.orderking.domain.cart.dto.CartResponseDto;
import com.sparta.orderking.domain.cart.entity.Cart;
import com.sparta.orderking.domain.cart.repository.CartRepository;
import com.sparta.orderking.domain.menu.entity.Menu;
import com.sparta.orderking.domain.menu.repository.MenuRepository;
import com.sparta.orderking.domain.store.entity.Store;
import com.sparta.orderking.domain.store.service.StoreService;
import com.sparta.orderking.domain.user.entity.User;
import com.sparta.orderking.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final MenuRepository menuRepository;
    private final UserService userService;
    private final StoreService storeService;

    @Transactional
    public CartResponseDto addMenu(Long userId, Long storeId, CartRequestDto requestDto) {
        User user = userService.findUser(userId);
        Store store = storeService.findStore(storeId);

        Cart cart = findCart(user, store);

        List<Menu> menuList = menuRepository.findAllById(requestDto.getMenuList());

        for (Menu menu : menuList) {
            if(menu.getStore().getId().equals(storeId)) {
                cart.addMenu(menu);
            } else {
                // 다른 가게의 메뉴 추가 시 장바구니 비운 후 메뉴 추가
                cart.clear();
                cart.addMenu(menu);
            }
        }

        cartRepository.save(cart);

        return new CartResponseDto(cart);
    }

    @Transactional()
    public CartResponseDto getCart(Long userId) {
        User user = userService.findUser(userId);
        Cart cart = getCart(user);

        return new CartResponseDto(cart);
    }

    @Transactional
    public CartResponseDto clearCart(Long userId) {
        User user = userService.findUser(userId);
        Cart cart = getCart(user);

        cart.clear();

        return new CartResponseDto(cart);
    }

    @Transactional
    public CartResponseDto removeMenu(Long userId, Long menuId) {
        User user = userService.findUser(userId);
        Cart cart = getCart(user);

        Menu menu = menuRepository.findById(menuId).orElseThrow();

        cart.removeMenu(menu);

        return new CartResponseDto(cart);
    }

    public Cart getCart(User user) {
        Optional<Cart> optionalCart = cartRepository.findByUser(user);

        Cart cart = optionalCart.orElseGet(Cart::new);

        return cart;
    }

    public Cart findCart(User user, Store store) {
        Optional<Cart> optionalCart = cartRepository.findByUser(user);

        if (optionalCart.isEmpty()) {
            Cart newCart = new Cart(user, store);
            return cartRepository.save(newCart);
        }

        return optionalCart.get();
    }

    // 매 시간마다 24시간이 지난 Cart를 삭제하는 스케줄러
    @Scheduled(cron = "0 0 * * * *") // 매 정시에 실행 (매 시간 0분 0초)
    @Transactional
    public void deleteOldCarts() {
        // 24시간 전 시간을 기준으로 삭제할 카트를 찾음
        LocalDateTime expirateTime = LocalDateTime.now().minusHours(24);
        List<Cart> oldCarts = cartRepository.findAllByLastUpdatedBefore(expirateTime);

        // 찾은 카트를 삭제
        cartRepository.deleteAll(oldCarts);
    }
}
