package com.sparta.orderking.store.delete;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuRepository extends JpaRepository<Menu,Long> {
    List<Menu> findMenuByStoreId(Long storeId);
}
