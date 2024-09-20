package com.sparta.orderking.domain.menu.repository;

import com.sparta.orderking.domain.menu.entity.Menu;
import com.sparta.orderking.domain.menu.entity.MenuPossibleEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Long> {
    default List<Menu> findAllByStoreAndMenuPossibleEnumNot(Long storeId, MenuPossibleEnum status) {
        return null;
    }
}
