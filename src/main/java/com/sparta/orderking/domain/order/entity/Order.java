package com.sparta.orderking.domain.order.entity;

import com.sparta.orderking.common.BaseEntity;
import com.sparta.orderking.domain.order.enums.OrderStatus;
import com.sparta.orderking.domain.store.entity.Store;
import com.sparta.orderking.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "orders")
public class Order extends BaseEntity {
    @Column(name = "price")
    private int price;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderMenu> menuList = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    public Order(User user, Store store) {
        this.user = user;
        this.store = store;
    }

    public void addMenu(OrderMenu orderMenu) {
        this.menuList.add(orderMenu);
        this.price = this.price + orderMenu.getMenu().getMenuPrice();
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }
}