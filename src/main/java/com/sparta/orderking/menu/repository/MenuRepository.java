package com.sparta.orderking.menu.repository;

import com.sparta.orderking.menu.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<Menu, Long> {
}
