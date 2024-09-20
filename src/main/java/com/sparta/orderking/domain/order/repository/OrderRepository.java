package com.sparta.orderking.domain.order.repository;

import com.sparta.orderking.domain.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}