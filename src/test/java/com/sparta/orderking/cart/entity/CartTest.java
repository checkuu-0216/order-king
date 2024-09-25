package com.sparta.orderking.cart.entity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.sparta.orderking.domain.cart.entity.Cart;
import com.sparta.orderking.domain.menu.entity.Menu;
import com.sparta.orderking.domain.store.entity.Store;
import com.sparta.orderking.domain.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

public class CartTest {

    private Cart cart;
    private User user;
    private Store store;
    private Menu menu;

    @BeforeEach
    public void setUp() {
        user = new User();
        ReflectionTestUtils.setField(user, "id", 1L);

        store = new Store();
        ReflectionTestUtils.setField(store, "id", 1L);

        cart = new Cart(user, store);

        menu = new Menu();
        ReflectionTestUtils.setField(menu, "id", 1L);
        ReflectionTestUtils.setField(menu, "menuPrice", 10000);
        ReflectionTestUtils.setField(menu, "store", store);
    }

    @Test
    public void addMenu_메뉴추가성공() {
        // When
        cart.addMenu(menu);

        // Then
        assertEquals(1, cart.getCartMenuList().size());
        assertEquals(10000, cart.getTotalPrice());
        assertNotNull(cart.getLastUpdated());
    }

    @Test
    public void clear_장바구니초기화성공() {
        // Given
        cart.addMenu(menu);

        // When
        cart.clear();

        // Then
        assertTrue(cart.getCartMenuList().isEmpty());
        assertEquals(0, cart.getTotalPrice());
        assertNull(cart.getStore());
        assertNotNull(cart.getLastUpdated());
    }

    @Test
    public void removeMenu_메뉴제거성공() {
        // Given
        cart.addMenu(menu);

        // When
        cart.removeMenu(menu);

        // Then
        assertTrue(cart.getCartMenuList().isEmpty());
        assertEquals(0, cart.getTotalPrice());
        assertNotNull(cart.getLastUpdated());
    }

    @Test
    public void removeMenu_존재하지않는메뉴제거() {
        // Given
        cart.addMenu(menu);

        Menu anotherMenu = new Menu();
        ReflectionTestUtils.setField(anotherMenu, "id", 2L);
        ReflectionTestUtils.setField(anotherMenu, "menuPrice", 15000);

        // When
        cart.removeMenu(anotherMenu);

        // Then
        assertEquals(1, cart.getCartMenuList().size());
        assertEquals(10000, cart.getTotalPrice());
    }
}
