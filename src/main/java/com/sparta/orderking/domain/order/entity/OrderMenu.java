package com.sparta.orderking.domain.order.entity;

import com.sparta.orderking.common.BaseEntity;
import com.sparta.orderking.domain.menu.entity.Menu;
import com.sparta.orderking.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "order_menu")
@Getter
@NoArgsConstructor
public class OrderMenu extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id")
    private Menu menu;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orders_id")
    private Order order;

    public OrderMenu(User user, Menu menu, Order order) {
        this.user = user;
        this.menu = menu;
        this.order = order;
    }
}