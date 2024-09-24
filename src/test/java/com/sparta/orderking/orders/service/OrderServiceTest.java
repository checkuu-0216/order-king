package com.sparta.orderking.orders.service;

import com.sparta.orderking.domain.cart.entity.Cart;
import com.sparta.orderking.domain.cart.repository.CartRepository;
import com.sparta.orderking.domain.menu.entity.Menu;
import com.sparta.orderking.domain.menu.repository.MenuRepository;
import com.sparta.orderking.domain.order.dto.CreateOrderRequestDto;
import com.sparta.orderking.domain.order.dto.UpdateOrderStatusRequestDto;
import com.sparta.orderking.domain.order.entity.Order;
import com.sparta.orderking.domain.order.entity.OrderMenu;
import com.sparta.orderking.domain.order.enums.OrderStatus;
import com.sparta.orderking.domain.order.repository.OrderMenuRepository;
import com.sparta.orderking.domain.order.repository.OrderRepository;
import com.sparta.orderking.domain.order.service.OrderService;
import com.sparta.orderking.domain.store.entity.Store;
import com.sparta.orderking.domain.store.repository.StoreRepository;
import com.sparta.orderking.domain.user.entity.User;
import com.sparta.orderking.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @InjectMocks
    OrderService orderService;

    @Mock
    private StoreRepository storeRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private OrderMenuRepository orderMenuRepository;

    @Mock
    private CartRepository cartRepository;

//    private User user;
//    private Store store;
//    private Cart cart;
//    private Menu menu;
//    @BeforeEach
//    void setUp() {
//        user = new User();
//        ReflectionTestUtils.setField(user, "id", 1L);
//
//        store = new Store();
//        ReflectionTestUtils.setField(store, "id", 1L);
//        ReflectionTestUtils.setField(store, "setOpenTime", LocalTime.of(9, 0));
//        ReflectionTestUtils.setField(store, "setCloseTime", LocalTime.of(22, 0));
//        ReflectionTestUtils.setField(store, "setMinPrice", 10000);
//
//        menu = new Menu();
//        ReflectionTestUtils.setField(menu, "setMenuPrice", 5000);
//
//        cart = new Cart(user, store);
//        cart.addMenu(menu);
//    }

    @Test
    public void createOrder_성공() {
        // given
        Long userId = 1L;
        Long storeId = 1L;

        User user = new User();
        Store store = new Store();
        ReflectionTestUtils.setField(store, "minPrice", 10000);
        ReflectionTestUtils.setField(store, "openTime", LocalTime.now().minusHours(1));
        ReflectionTestUtils.setField(store, "closeTime", LocalTime.now().plusHours(1));

        Menu chicken = new Menu();
        ReflectionTestUtils.setField(chicken, "menuPrice", 10000);
        Menu pizza = new Menu();
        ReflectionTestUtils.setField(pizza, "menuPrice", 10000);


        Cart userCart = new Cart(user, store);
        userCart.addMenu(chicken);
        userCart.addMenu(pizza);

        Order order = new Order(user, store);

        given(userRepository.findById(userId)).willReturn(Optional.of(user));
        given(storeRepository.findById(storeId)).willReturn(Optional.of(store));
        given(menuRepository.findById(1L)).willReturn(Optional.of(chicken));
        given(menuRepository.findById(2L)).willReturn(Optional.of(pizza));
        given(cartRepository.findByUser(user)).willReturn(Optional.of(userCart));

        // when
        orderService.createOrder(userId, storeId);

        // then
        verify(orderRepository).save(any(Order.class));
        verify(orderMenuRepository, times(2)).save(any(OrderMenu.class));
    }

    @Test
    public void createOrder_최소주문금액실패() {
        // given
        Long userId = 1L;
        Long storeId = 1L;

        User user = new User();
        Store store = new Store();
        ReflectionTestUtils.setField(store, "minPrice", 10000);

        Cart userCart = new Cart(user, store);
        ReflectionTestUtils.setField(userCart, "totalPrice", 5000);

        given(storeRepository.findById(storeId)).willReturn(Optional.of(store));

        // when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> orderService.createOrder(userId, storeId));

        // then
        assertEquals("최소 주문 금액 보다 적습니다.", exception.getMessage());
    }

    @Test
    public void createOrder_가게오픈전_주문실패() {
        // given
        Long userId = 1L;
        Long storeId = 1L;
        CreateOrderRequestDto requestDto = new CreateOrderRequestDto();
        ReflectionTestUtils.setField(requestDto, "price", 10000);

        Store store = new Store();
        ReflectionTestUtils.setField(store, "minPrice", 5000);
        ReflectionTestUtils.setField(store, "openTime", LocalTime.now().plusHours(1));

        given(storeRepository.findById(storeId)).willReturn(Optional.of(store));

        // when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> orderService.createOrder(userId, storeId));

        // then
        assertEquals("가게 오픈시간 전입니다.", exception.getMessage());
    }

    @Test
    public void createOrder_가게마감후_주문실패() {
        // given
        Long userId = 1L;
        Long storeId = 1L;
        CreateOrderRequestDto requestDto = new CreateOrderRequestDto();
        ReflectionTestUtils.setField(requestDto, "price", 10000);

        Store store = new Store();
        ReflectionTestUtils.setField(store, "minPrice", 5000);
        ReflectionTestUtils.setField(store, "openTime", LocalTime.now().minusHours(3));
        ReflectionTestUtils.setField(store, "closeTime", LocalTime.now().minusHours(1));

        given(storeRepository.findById(storeId)).willReturn(Optional.of(store));

        // when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> orderService.createOrder(userId, storeId));

        // then
        assertEquals("가게 마감시간 후입니다.", exception.getMessage());
    }

    @Test
    public void updateOrderStatus_성공() {
        // given
        Long userId = 1L;
        Long storeId = 1L;
        Long orderId = 1L;
        UpdateOrderStatusRequestDto requestDto = new UpdateOrderStatusRequestDto();
        ReflectionTestUtils.setField(requestDto, "orderStatus", OrderStatus.PREPARING);
    }
}
