package com.sparta.orderking.menu.repository;

import com.sparta.orderking.config.AuthUser;
import com.sparta.orderking.config.User;
import com.sparta.orderking.domain.store.entity.Store;
import com.sparta.orderking.menu.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MenuRepository extends JpaRepository<Menu, Long> {
        Optional<Menu> findByUserAndStore(AuthUser authUser, Store store);
        List<Menu> findMenuByStoreId(Long storeId);
}
