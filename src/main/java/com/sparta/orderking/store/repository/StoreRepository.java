package com.sparta.orderking.store.repository;

import com.sparta.orderking.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StoreRepository extends JpaRepository<Store,Long> {
    List<Store> findByName(String name);
}
