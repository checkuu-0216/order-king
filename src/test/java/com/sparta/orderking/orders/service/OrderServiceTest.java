package com.sparta.orderking.orders.service;

import com.sparta.orderking.domain.cart.entity.Cart;
import com.sparta.orderking.domain.cart.repository.CartRepository;
import com.sparta.orderking.domain.cart.service.CartService;
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
import com.sparta.orderking.domain.store.service.StoreService;
import com.sparta.orderking.domain.user.entity.User;
import com.sparta.orderking.domain.user.entity.UserEnum;
import com.sparta.orderking.domain.user.repository.UserRepository;
import com.sparta.orderking.domain.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
    private OrderRepository orderRepository;

    @Mock
    private OrderMenuRepository orderMenuRepository;

    @Mock
    private UserService userService;

    @Mock
    private StoreService storeService;

    @Mock
    private CartService cartService;

    private User user;
    private Store store;
    private Cart cart;
    private Order order;
    @BeforeEach
    void setUp() {
        Long userId = 1L;
        Long storeId = 1L;

        user = new User();
        store = new Store();
        cart = new Cart(user, store);
        order = new Order(user, store, OrderStatus.PENDING);

        ReflectionTestUtils.setField(store, "id", storeId);
        ReflectionTestUtils.setField(store, "minPrice", 10000);
        ReflectionTestUtils.setField(store, "openTime", LocalTime.of(9, 0)); // 가게 오픈 시간 설정
        ReflectionTestUtils.setField(store, "closeTime", LocalTime.of(21, 0)); // 가게 마감 시간 설정

        Menu chicken = new Menu();
        ReflectionTestUtils.setField(chicken, "menuPrice", 10000);
        Menu pizza = new Menu();
        ReflectionTestUtils.setField(pizza, "menuPrice", 10000);
        cart.addMenu(chicken);
        cart.addMenu(pizza);
    }

    @Test
    public void createOrder_성공() {
        // Given
        long userId = 1L;
        long storeId = 1L;

        given(userService.findUser(anyLong())).willReturn(user);
        given(storeService.findStore(anyLong())).willReturn(store);
        given(cartService.findCart(user)).willReturn(cart);

        // When
        orderService.createOrder(userId, storeId);

        // Then
        verify(orderRepository).save(any(Order.class));
        verify(orderMenuRepository).saveAll(any(List.class));
    }

    @Test
    public void createOrder_최소주문금액실패() {
        // Given
        long userId = 1L;
        long storeId = 1L;

        ReflectionTestUtils.setField(store, "minPrice", 30000);

        given(userService.findUser(anyLong())).willReturn(user);
        given(storeService.findStore(anyLong())).willReturn(store);
        given(cartService.findCart(user)).willReturn(cart);

        // when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> orderService.createOrder(userId, storeId));

        // then
        assertEquals("최소 주문 금액 보다 적습니다.", exception.getMessage());
    }

    @Test
    public void createOrder_가게오픈전_주문실패() {
        // given
        LocalTime now = LocalTime.of(8, 0);

        // when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> orderService.checkStoreBusinessTimes(store, now));

        // then
        assertEquals("가게 오픈시간 전입니다.", exception.getMessage());
    }

    @Test
    public void createOrder_가게마감후_주문실패() {
        // given
        LocalTime now = LocalTime.of(23, 0);

        // when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> orderService.checkStoreBusinessTimes(store, now));

        // then
        assertEquals("가게 마감시간 후입니다.", exception.getMessage());
    }

    @Test
    public void updateOrderStatus_성공() {
        // given
        Long userId = 1L;
        Long storeId = 1L;
        Long orderId = 1L;

        ReflectionTestUtils.setField(user, "userEnum", UserEnum.OWNER);
        ReflectionTestUtils.setField(store, "user", user);
        ReflectionTestUtils.setField(order, "orderStatus", OrderStatus.PENDING);

        UpdateOrderStatusRequestDto requestDto = new UpdateOrderStatusRequestDto();
        ReflectionTestUtils.setField(requestDto, "orderStatus", OrderStatus.PREPARING);

        given(userService.findUser(anyLong())).willReturn(user);
        given(storeService.findStore(anyLong())).willReturn(store);
        given(orderRepository.findById(anyLong())).willReturn(Optional.of(order));

        // when
        orderService.updateOrderStatus(userId, storeId, orderId, requestDto);

        // then
        verify(orderRepository).save(any(Order.class));
    }

    @Test
    public void updateOrderStatus_권한없음() {
        // given
        Long userId = 2L; // 다른 사용자
        Long storeId = 1L;
        Long orderId = 1L;

        ReflectionTestUtils.setField(user, "id", 1L);
        ReflectionTestUtils.setField(user, "userEnum", UserEnum.USER);
        ReflectionTestUtils.setField(store, "user", user);
        ReflectionTestUtils.setField(order, "orderStatus", OrderStatus.PENDING);

        UpdateOrderStatusRequestDto requestDto = new UpdateOrderStatusRequestDto();
        ReflectionTestUtils.setField(requestDto, "orderStatus", OrderStatus.PREPARING);

        given(userService.findUser(anyLong())).willReturn(user);
        given(storeService.findStore(anyLong())).willReturn(store);
        given(orderRepository.findById(anyLong())).willReturn(Optional.of(order));

        // when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            orderService.updateOrderStatus(userId, storeId, orderId, requestDto);
        });

        // then
        assertEquals("주문 상태를 변경할 권한이 없습니다.", exception.getMessage());
    }

    @Test
    public void updateOrderStatus_해당가게주문아님() {
        // given
        Long userId = 1L;
        Long storeId = 1L;
        Long orderId = 1L;
        Long wrongStoreId = 2L;

        ReflectionTestUtils.setField(user, "userEnum", UserEnum.OWNER);
        ReflectionTestUtils.setField(store, "user", user);
        ReflectionTestUtils.setField(store, "id", wrongStoreId);
        ReflectionTestUtils.setField(order, "store", store);
        ReflectionTestUtils.setField(order, "orderStatus", OrderStatus.PENDING);

        UpdateOrderStatusRequestDto requestDto = new UpdateOrderStatusRequestDto();
        ReflectionTestUtils.setField(requestDto, "orderStatus", OrderStatus.PREPARING);

        given(userService.findUser(anyLong())).willReturn(user);
        given(storeService.findStore(anyLong())).willReturn(store);
        given(orderRepository.findById(anyLong())).willReturn(Optional.of(order));

        // when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            orderService.updateOrderStatus(userId, storeId, orderId, requestDto);
        });

        // then
        assertEquals("해당 가게의 주문이 아닙니다.", exception.getMessage());
    }

    @Test
    public void updateOrderStatus_상태변경불가() {
        // given
        Long userId = 1L;
        Long storeId = 1L;
        Long orderId = 1L;

        ReflectionTestUtils.setField(user, "userEnum", UserEnum.OWNER);
        ReflectionTestUtils.setField(store, "user", user);
        ReflectionTestUtils.setField(order, "orderStatus", OrderStatus.DELIVERY_COMPLETED);

        UpdateOrderStatusRequestDto requestDto = new UpdateOrderStatusRequestDto();
        ReflectionTestUtils.setField(requestDto, "orderStatus",  OrderStatus.PREPARING);

        given(userService.findUser(userId)).willReturn(user);
        given(storeService.findStore(storeId)).willReturn(store);
        given(orderRepository.findById(orderId)).willReturn(Optional.of(order));

        // when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            orderService.updateOrderStatus(userId, storeId, orderId, requestDto);
        });

        // then
        assertEquals("주문 상태를 변경할 수 없습니다.", exception.getMessage());
    }

    @Test
    public void updateOrderStatus_주문취소불가() {
        // given
        Long userId = 1L;
        Long storeId = 1L;
        Long orderId = 1L;

        ReflectionTestUtils.setField(user, "userEnum", UserEnum.OWNER);
        ReflectionTestUtils.setField(store, "user", user);
        ReflectionTestUtils.setField(order, "orderStatus", OrderStatus.PREPARING);

        UpdateOrderStatusRequestDto requestDto = new UpdateOrderStatusRequestDto();
        ReflectionTestUtils.setField(requestDto, "orderStatus", OrderStatus.WITHDRAW);

        given(userService.findUser(userId)).willReturn(user);
        given(storeService.findStore(storeId)).willReturn(store);
        given(orderRepository.findById(orderId)).willReturn(Optional.of(order));

        // when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            orderService.updateOrderStatus(userId, storeId, orderId, requestDto);
        });

        // then
        assertEquals("현재는 주문을 취소할 수 없습니다.", exception.getMessage());
    }
}
