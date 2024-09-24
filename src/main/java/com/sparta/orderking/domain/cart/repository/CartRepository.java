package com.sparta.orderking.domain.cart.repository;

import com.sparta.orderking.domain.cart.entity.Cart;
import com.sparta.orderking.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUser(User user);

    List<Cart> findAllByLastUpdatedBefore(LocalDateTime expirateTime);
}
