package com.sparta.orderking.domain.store.repository;

import com.sparta.orderking.domain.store.entity.Store;
import com.sparta.orderking.domain.store.entity.StoreStatus;
import com.sparta.orderking.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StoreRepository extends JpaRepository<Store,Long> {
    List<Store> findByNameAndStoreStatus(String name, StoreStatus serviceEnum);

    List<Store> findByUser(User user);
}
