package com.sparta.orderking.domain.order.repository;

import com.sparta.orderking.domain.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("SELECT o FROM Order o JOIN FETCH o.menuList om JOIN FETCH om.menu WHERE o.id = :orderId")
    Order findByIdWithMenus(Long orderId);
}