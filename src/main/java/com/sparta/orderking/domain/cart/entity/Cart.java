package com.sparta.orderking.domain.cart.entity;

import com.sparta.orderking.common.BaseEntity;
import com.sparta.orderking.domain.menu.entity.Menu;
import com.sparta.orderking.domain.store.entity.Store;
import com.sparta.orderking.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Cart extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    private List<Menu> cartMenuList = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    @Column
    private LocalTime lastUpdated;

    public Cart(User user, Store store) {
        this.user = user;
        this.store = store;
        this.lastUpdated = LocalTime.now();
    }

    public void addMenu(Menu menu) {
        this.cartMenuList.add(menu);
        this.lastUpdated = LocalTime.now();  // 마지막 업데이트 시간 갱신
    }

    public void clear() {
        this.cartMenuList.clear();
        this.store = null;
        this.lastUpdated = LocalTime.now();
    }
}
