package com.sparta.orderking.domain.menu.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.sparta.orderking.common.BaseEntity;
import com.sparta.orderking.domain.menu.dto.MenuRequestDto;
import com.sparta.orderking.domain.menu.dto.MenuUpdateRequestDto;
import com.sparta.orderking.domain.store.entity.Store;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Menu extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "menu_id")
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "store")
    private Store store;

    @Column(name = "menuName")
    private String menuName;

    @Column(name = "menuInfo")
    private String menuInfo;

    @Column(name = "menuPrice")
    private Integer menuPrice;

    @Column(name = "menuImg")
    private String menuImg;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private MenuPossibleEnum possibleEnum;

    public Menu(MenuRequestDto requestDto, Store store) {
        this.menuName = requestDto.getMenuName();
        this.menuInfo = requestDto.getMenuInfo();
        this.menuPrice = requestDto.getMenuPrice();
        this.menuImg = requestDto.getMenuImg();
        this.possibleEnum = requestDto.getMenuPossibleEnum();
        this.store = store;
    }

    public void updateMenu(MenuUpdateRequestDto requestDto, Store store) {
        this.menuName = requestDto.getMenuName();
        this.menuInfo = requestDto.getMenuInfo();
        this.menuPrice = requestDto.getMenuPrice();
        this.menuImg = requestDto.getMenuImg();
        this.possibleEnum = requestDto.getMenuPossibleEnum();
        this.store = store;
    }

    public void deleteMenu(MenuPossibleEnum possibleEnum) {
        this.possibleEnum = possibleEnum;
    }

}

