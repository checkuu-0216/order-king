package com.sparta.orderking.cart.service;

import com.sparta.orderking.domain.cart.dto.CartRequestDto;
import com.sparta.orderking.domain.cart.dto.CartResponseDto;
import com.sparta.orderking.domain.cart.entity.Cart;
import com.sparta.orderking.domain.cart.repository.CartRepository;
import com.sparta.orderking.domain.cart.service.CartService;
import com.sparta.orderking.domain.menu.entity.Menu;
import com.sparta.orderking.domain.menu.repository.MenuRepository;
import com.sparta.orderking.domain.store.entity.Store;
import com.sparta.orderking.domain.store.service.StoreService;
import com.sparta.orderking.domain.user.entity.User;
import com.sparta.orderking.domain.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class CartServiceTest {
    @Mock
    private CartRepository cartRepository;

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private UserService userService;

    @Mock
    private StoreService storeService;

    @InjectMocks
    private CartService cartService;

    private User user;
    private Store store;
    private Cart cart;
    private Menu menu;
    private CartRequestDto cartRequestDto;

    @BeforeEach
    public void setUp() {
        Long storeId = 1L;

        user = new User();
        store = new Store();
        ReflectionTestUtils.setField(store, "id", storeId);
        cart = new Cart(user, store);
        menu = new Menu();
        ReflectionTestUtils.setField(menu, "store", store);
        ReflectionTestUtils.setField(menu, "menuPrice", 12000);

        cartRequestDto = new CartRequestDto();
        ReflectionTestUtils.setField(cartRequestDto, "menuList", Arrays.asList(1L, 2L));
    }

    @Test
    public void addMenu_성공() {
        // Given
        given(userService.findUser(anyLong())).willReturn(user);
        given(storeService.findStore(anyLong())).willReturn(store);
        given(cartRepository.findByUser(user)).willReturn(Optional.of(cart));
        given(menuRepository.findAllById(anyList())).willReturn(Arrays.asList(menu));

        // When
        CartResponseDto response = cartService.addMenu(1L, 1L, cartRequestDto);

        // Then
        verify(cartRepository).save(cart);
        assertNotNull(response);
        assertEquals(cart.getCartMenuList().size(), 1);
    }

    @Test
    public void addMenu_다른가게메뉴_추가시_장바구니초기화() {
        // Given
        Store differentStore = new Store();
        ReflectionTestUtils.setField(differentStore, "id", 2L);

        Menu differentStoreMenu = new Menu();
        ReflectionTestUtils.setField(differentStoreMenu, "id", 3L);
        ReflectionTestUtils.setField(differentStoreMenu, "store", differentStore);
        ReflectionTestUtils.setField(differentStoreMenu, "menuPrice", 13000);

        CartRequestDto cartRequestDto = new CartRequestDto(); // 필요시 CartRequestDto 초기화
        ReflectionTestUtils.setField(cartRequestDto, "menuList", List.of(3L));

        given(menuRepository.findAllById(anyList())).willReturn(List.of(differentStoreMenu));
        given(cartRepository.findByUser(any(User.class))).willReturn(Optional.of(cart));
        given(cartRepository.save(any(Cart.class))).willReturn(cart);

        // When
        cartService.addMenu(1L, 2L, cartRequestDto);

        // Then
        verify(cart).clear();
        verify(cart).addMenu(differentStoreMenu);
        verify(cartRepository).save(any(Cart.class));
    }
}
