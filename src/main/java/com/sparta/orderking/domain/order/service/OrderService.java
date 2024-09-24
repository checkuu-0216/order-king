package com.sparta.orderking.domain.order.service;

import com.sparta.orderking.domain.cart.entity.Cart;
import com.sparta.orderking.domain.cart.repository.CartRepository;
import com.sparta.orderking.domain.menu.entity.Menu;
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

import java.time.LocalTime;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderMenuRepository orderMenuRepository;
    private final StoreRepository storeRepository;
    private final UserRepository userRepository;
    private final CartRepository cartRepository;

    @Transactional
    public void createOrder(Long userId, Long storeId) {
        Store store = storeRepository.findById(storeId).orElseThrow();

        // 가게의 오픈 / 마감 시간 확인
        LocalTime now = LocalTime.now();

        // 메서드 따로 빼보기
        if(now.isBefore(store.getOpenTime())) {
            throw new IllegalArgumentException("가게 오픈시간 전입니다.");
        }

        if (now.isAfter(store.getCloseTime())) {
            throw new IllegalArgumentException("가게 마감시간 후입니다.");
        }

        User user = userRepository.findById(userId).orElseThrow();
        Cart userCart = cartRepository.findByUser(user); // null 체크 optional

        // 가게에서 설정한 최소 주문 금액 만족 여부
        if (store.getMinPrice() > userCart.getTotalPrice()) {
            throw new IllegalArgumentException("최소 주문 금액 보다 적습니다.");
        }

        Order order = new Order(user, store); // orderStatus

        for(Menu menu : userCart.getCartMenuList()) {
//            Menu menu = menuRepository.findById(menuId).orElseThrow();
            OrderMenu orderMenu = new OrderMenu(user, menu, order);
            orderMenuRepository.save(orderMenu); // saveAll(list)
            order.addMenu(orderMenu);
        }
        order.setOrderStatus(OrderStatus.PENDING);
        orderRepository.save(order);
    }

    @Transactional
    public void updateOrderStatus(Long userId, Long storeId, Long orderId, UpdateOrderStatusRequestDto requestDto) {
        User user = userRepository.findById(userId).orElseThrow();
        Store store = storeRepository.findById(storeId).orElseThrow();

        if (!user.getUserEnum().equals(UserEnum.OWNER) && !store.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("주문 상태를 변경할 권한이 없습니다.");
        }

        Order order = orderRepository.findById(orderId).orElseThrow();


        // 따로 빼기
        if(!order.getStore().getId().equals(storeId)) {
            throw new IllegalArgumentException("해당 가게의 주문이 아닙니다.");
        }

        // 이전 상태 확인
        order.setOrderStatus(requestDto.getOrderStatus());
    }

    // authUser, storeUser
    public OrderResponseDto getOrder(Long orderId) {
        Order order = orderRepository.findByIdWithMenus(orderId);
        OrderResponseDto orderResponseDto = new OrderResponseDto(order);

        return orderResponseDto;
    }
}