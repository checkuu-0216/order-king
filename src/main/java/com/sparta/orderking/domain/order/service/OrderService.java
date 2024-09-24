package com.sparta.orderking.domain.order.service;

import com.sparta.orderking.domain.cart.entity.Cart;
import com.sparta.orderking.domain.cart.service.CartService;
import com.sparta.orderking.domain.menu.entity.Menu;
import com.sparta.orderking.domain.order.dto.OrderResponseDto;
import com.sparta.orderking.domain.order.dto.UpdateOrderStatusRequestDto;
import com.sparta.orderking.domain.order.entity.Order;
import com.sparta.orderking.domain.order.entity.OrderMenu;
import com.sparta.orderking.domain.order.enums.OrderStatus;
import com.sparta.orderking.domain.order.repository.OrderMenuRepository;
import com.sparta.orderking.domain.order.repository.OrderRepository;
import com.sparta.orderking.domain.store.entity.Store;
import com.sparta.orderking.domain.store.service.StoreService;
import com.sparta.orderking.domain.user.entity.User;
import com.sparta.orderking.domain.user.entity.UserEnum;
import com.sparta.orderking.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderMenuRepository orderMenuRepository;
    private final CartService cartService;
    private final UserService userService;
    private final StoreService storeService;

    @Transactional
    public void createOrder(Long userId, Long storeId) {
        Store store = storeService.findStore(storeId);

        // 가게 영업시간 체크
        LocalTime now = LocalTime.now();
        checkStoreBusinessTimes(store, now);

        User user = userService.findUser(userId);
        Cart userCart = cartService.findCart(user);

        // 가게 최소 주문 금액 체크
        checkStoreMinPrice(store, userCart);

        Order order = new Order(user, store, OrderStatus.PENDING);
        List<OrderMenu> orderMenuList = new ArrayList<>();
        for(Menu menu : userCart.getCartMenuList()) {
            OrderMenu orderMenu = new OrderMenu(user, menu, order);
            orderMenuList.add(orderMenu);
            order.addMenu(orderMenu);
        }
        orderMenuRepository.saveAll(orderMenuList);
        orderRepository.save(order);
    }

    @Transactional
    public void updateOrderStatus(Long userId, Long storeId, Long orderId, UpdateOrderStatusRequestDto requestDto) {
        User user = userService.findUser(userId);
        Store store = storeService.findStore(storeId);
        Order order = orderRepository.findById(orderId).orElseThrow();

        checkStoreOrder(order, storeId);

        if (!user.getUserEnum().equals(UserEnum.OWNER) && !store.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("주문 상태를 변경할 권한이 없습니다.");
        }

        // 이전 상태 확인
        if(order.getOrderStatus().getValue() > requestDto.getOrderStatus().getValue()) {
            throw new IllegalArgumentException("주문 상태를 변경할 수 없습니다.");
        }

        // 주문 취소 할 수 있는지 체크
        if(requestDto.getOrderStatus().getValue() == 5) {
            isWithdrawable(order.getOrderStatus());
        }

        order.updateOrderStatus(requestDto.getOrderStatus());

        orderRepository.save(order);
    }

    public OrderResponseDto getOrder(Long userId, Long orderId) {
        Order order = orderRepository.findByIdWithMenus(orderId);

        if(!order.getUser().getId().equals(userId) || !order.getStore().getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("해당 주문을 조회할 권한이 없습니다.");
        }

        OrderResponseDto orderResponseDto = new OrderResponseDto(order);

        return orderResponseDto;
    }

    public void checkStoreBusinessTimes(Store store, LocalTime now) {
        // 가게의 오픈 / 마감 시간 확인
//        LocalTime now = LocalTime.now();

        if(now.isBefore(store.getOpenTime())) {
            throw new IllegalArgumentException("가게 오픈시간 전입니다.");
        }

        if(now.isAfter(store.getCloseTime())) {
            throw new IllegalArgumentException("가게 마감시간 후입니다.");
        }
    }

    public void checkStoreMinPrice(Store store, Cart cart) {
        // 가게에서 설정한 최소 주문 금액 만족 여부
        if (store.getMinPrice() > cart.getTotalPrice()) {
            throw new IllegalArgumentException("최소 주문 금액 보다 적습니다.");
        }
    }

    public void checkStoreOrder(Order order, Long storeId) {
        if(!order.getStore().getId().equals(storeId)) {
            throw new IllegalArgumentException("해당 가게의 주문이 아닙니다.");
        }
    }

    public void isWithdrawable(OrderStatus orderStatus) {
        // PENDING 일때만 취소 가능
        if(orderStatus.getValue() != 1) {
            throw new IllegalArgumentException("현재는 주문을 취소할 수 없습니다.");
        }
    }
}