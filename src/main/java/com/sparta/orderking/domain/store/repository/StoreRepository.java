package com.sparta.orderking.domain.store.repository;

import com.sparta.orderking.domain.store.entity.Store;
import com.sparta.orderking.domain.store.entity.StoreStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StoreRepository extends JpaRepository<Store,Long> {
    List<Store> findByNameAndStoreStatus(String name, StoreStatus serviceEnum);
}
