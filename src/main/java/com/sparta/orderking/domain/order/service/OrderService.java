package com.sparta.orderking.domain.order.service;

import com.sparta.orderking.domain.menu.entity.Menu;
import com.sparta.orderking.domain.menu.repository.MenuRepository;
import com.sparta.orderking.domain.order.dto.CreateOrderRequestDto;
import com.sparta.orderking.domain.order.dto.OrderResponseDto;
import com.sparta.orderking.domain.order.dto.UpdateOrderStatusRequestDto;
import com.sparta.orderking.domain.order.entity.Order;
import com.sparta.orderking.domain.order.entity.OrderMenu;
import com.sparta.orderking.domain.order.enums.OrderStatus;
import com.sparta.orderking.domain.order.repository.OrderMenuRepository;
import com.sparta.orderking.domain.order.repository.OrderRepository;
import com.sparta.orderking.domain.store.entity.Store;
import com.sparta.orderking.domain.store.repository.StoreRepository;
import com.sparta.orderking.domain.user.entity.User;
import com.sparta.orderking.domain.user.entity.UserEnum;
import com.sparta.orderking.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDateTime;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderMenuRepository orderMenuRepository;
    private final StoreRepository storeRepository;
    private final MenuRepository menuRepository;
    private final UserRepository userRepository;

    @Transactional
    public void createOrder(Long userId, Long storeId, CreateOrderRequestDto requestDto) {
        Store store = storeRepository.findById(storeId).orElseThrow();
        // 가게에서 설정한 최소 주문 금액 만족 여부
        if(store.getMinPrice() > requestDto.getPrice()) {
            throw new IllegalArgumentException("최소 주문 금액 보다 적습니다.");
        }
        // 가게의 오픈 / 마감 시간 확인
        LocalDateTime now = LocalDateTime.now();
        if(now.isBefore(ChronoLocalDateTime.from(store.getOpenTime()))) {
            throw new IllegalArgumentException("가게 오픈시간 전입니다.");
        }

        if(now.isAfter(ChronoLocalDateTime.from(store.getCloseTime()))) {
            throw new IllegalArgumentException("가게 마감시간 후입니다.");
        }

        User user = userRepository.findById(userId).orElseThrow();
        Order order = new Order(user, store, requestDto.getPrice());

        for(Long menuId : requestDto.getMenuList()) {
            Menu menu = menuRepository.findById(menuId).orElseThrow();
            OrderMenu orderMenu = new OrderMenu(user, menu, order);
            orderMenuRepository.save(orderMenu);
            order.addMenu(orderMenu);
            System.out.println(order.getMenuList());
        }
        order.setOrderStatus(OrderStatus.PENDING);
        orderRepository.save(order);
    }

    public void updateOrderStatus(Long userId, Long storeId, Long orderId, UpdateOrderStatusRequestDto requestDto) {
        User user = userRepository.findById(userId).orElseThrow();

        if(!user.getUserEnum().equals(UserEnum.OWNER)) {
            throw new IllegalArgumentException("주문을 수락할 권한이 없습니다.");
        }

        Order order = orderRepository.findById(orderId).orElseThrow();

        if(!order.getStore().getId().equals(storeId)) {
            throw new IllegalArgumentException("해당 가게의 주문이 아닙니다.");
        }

        order.setOrderStatus(requestDto.getOrderStatus());
    }

    public OrderResponseDto getOrder(Long orderId) {
        Order order = orderRepository.findByIdWithMenus(orderId);
        OrderResponseDto orderResponseDto = new OrderResponseDto(order);
        System.out.println(orderResponseDto);
        return orderResponseDto;
    }
}