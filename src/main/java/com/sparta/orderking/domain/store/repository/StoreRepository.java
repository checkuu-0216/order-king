package com.sparta.orderking.domain.store.repository;

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

    //가게이름에 특정 키워드 포함된 가게 검색
    List<Store> findByNameContaining(String keyword);
    //메뉴이름을 가진 가게 검색
    @Query("SELECT s from Store s join s.menus m where m.menuName LIKE %:keyword%")
    List<Store> findStoreByMenuName(@Param("keyword") String keyword);
}
