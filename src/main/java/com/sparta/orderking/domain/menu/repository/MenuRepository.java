package com.sparta.orderking.domain.menu.repository;

import com.sparta.orderking.config.AuthUser;
import com.sparta.orderking.domain.menu.entity.Menu;
import com.sparta.orderking.domain.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MenuRepository extends JpaRepository<Menu, Long> {
        List<Menu> findMenuByStoreId(Long storeId);
}
