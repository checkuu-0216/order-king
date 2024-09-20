package com.sparta.orderking.domain.review.entity;

import com.sparta.orderking.common.BaseEntity;
import com.sparta.orderking.domain.order.entity.Order;
import com.sparta.orderking.domain.store.entity.Store;
import com.sparta.orderking.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Review extends BaseEntity {
    @Column(name = "content")
    private String content;

    @Column(name = "point")
    private int point;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    public Review(User user, Order order, Store store, String content, int point) {
        this.user = user;
        this.order = order;
        this.store = store;
        this.content = content;
        this.point = point;
    }

    public void update(String content, int point) {
        this.content = content;
        this.point = point;
    }
}
