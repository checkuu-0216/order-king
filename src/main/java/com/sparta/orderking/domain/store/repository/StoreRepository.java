package com.sparta.orderking.domain.store.repository;

import com.sparta.orderking.domain.menu.entity.MenuCategoryEnum;
import com.sparta.orderking.domain.store.entity.Store;
import com.sparta.orderking.domain.store.entity.StoreStatus;
import com.sparta.orderking.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StoreRepository extends JpaRepository<Store, Long> {
    List<Store> findByNameAndStoreStatus(String name, StoreStatus serviceEnum);

    List<Store> findByUserAndStoreStatus(User user, StoreStatus storeStatus);

    @Query("SELECT s FROM Store s LEFT JOIN s.menus m WHERE s.name LIKE %:keyword% OR m.menuName LIKE %:keyword%")
    List<Store> findStoresByStoreNameOrMenuName(@Param("keyword") String keyword);

    @Query("SELECT s FROM Store s JOIN s.menus m WHERE m.menuCategoryEnum = :category")
    List<Store> findStoresByMenuCategory(@Param("category") MenuCategoryEnum category);
}
