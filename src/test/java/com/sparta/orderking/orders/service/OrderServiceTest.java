package com.sparta.orderking.orders.service;

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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

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

    @Test
    public void createOrder_성공() {
        // given
        Long userId = 1L;
        Long storeId = 1L;
        CreateOrderRequestDto requestDto = new CreateOrderRequestDto();
        ReflectionTestUtils.setField(requestDto, "price", 15000);
        ReflectionTestUtils.setField(requestDto, "menuList", Arrays.asList(1L, 2L));

        User user = new User();
        Store store = new Store();
        ReflectionTestUtils.setField(store, "minPrice", 10000);
        ReflectionTestUtils.setField(store, "openTime", LocalTime.now().minusHours(1));
        ReflectionTestUtils.setField(store, "closeTime", LocalTime.now().plusHours(1));

        Menu chicken = new Menu();
        Menu pizza = new Menu();

        Order order = new Order(user, store, requestDto.getPrice());

        given(userRepository.findById(userId)).willReturn(Optional.of(user));
        given(storeRepository.findById(storeId)).willReturn(Optional.of(store));
        given(menuRepository.findById(1L)).willReturn(Optional.of(chicken));
        given(menuRepository.findById(2L)).willReturn(Optional.of(pizza));

        // when
        orderService.createOrder(userId, storeId, requestDto);

        // then
        verify(orderRepository).save(any(Order.class));
        verify(orderMenuRepository, times(2)).save(any(OrderMenu.class));
    }

    @Test
    public void createOrder_최소주문금액실패() {
        // given
        Long userId = 1L;
        Long storeId = 1L;
        CreateOrderRequestDto requestDto = new CreateOrderRequestDto();
        ReflectionTestUtils.setField(requestDto, "price", 5000);

        Store store = new Store();
        ReflectionTestUtils.setField(store, "minPrice", 10000);

        given(storeRepository.findById(storeId)).willReturn(Optional.of(store));

        // when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> orderService.createOrder(userId, storeId, requestDto));

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
                () -> orderService.createOrder(userId, storeId, requestDto));

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
                () -> orderService.createOrder(userId, storeId, requestDto));

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
