package com.sparta.orderking.domain.menu.repository;

import com.sparta.orderking.domain.menu.entity.Menu;
import com.sparta.orderking.domain.menu.entity.MenuPossibleEnum;
import com.sparta.orderking.domain.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Long> {
    List<Menu> findAllByStoreAndMenuPossibleEnumNot(Store store, MenuPossibleEnum status);

    boolean existsByStoreAndMenuName (Store store,String name);
}
