package com.sparta.orderking.domain.store.repository;

import com.sparta.orderking.config.AuthUser;
import com.sparta.orderking.domain.menu.entity.Menu;
import com.sparta.orderking.domain.store.entity.Store;
import com.sparta.orderking.domain.store.entity.StoreServiceEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StoreRepository extends JpaRepository<Store,Long> {
    Optional<Store> findByUserAndStore(AuthUser authUser, Store store);
    List<Store> findByNameAndService(String name, StoreServiceEnum serviceEnum);
}
