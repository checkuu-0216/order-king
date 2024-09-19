package com.sparta.orderking.domain.menu.repository;

import com.sparta.orderking.domain.menu.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Long> {
        List<Menu> findMenuByStoreId(Long storeId);
}
