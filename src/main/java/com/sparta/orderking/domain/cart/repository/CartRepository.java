package com.sparta.orderking.domain.cart.repository;

import com.sparta.orderking.domain.cart.entity.Cart;
import com.sparta.orderking.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Cart findByUser(User user);
}
