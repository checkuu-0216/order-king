package com.sparta.orderking.domain.menu.dto;

import com.sparta.orderking.domain.menu.entity.Menu;
import com.sparta.orderking.domain.menu.entity.MenuPossibleEnum;
import lombok.Getter;

@Getter
public class MenuResponseDto {

    private final String menuName;
    private final String menuInfo;
    private final String menuPrice;
    private final String menuImg;
    private final MenuPossibleEnum menuPossibleEnum;

    public MenuResponseDto(String menuName, String menuInfo, String menuPrice, String menuImg, MenuPossibleEnum menuPossibleEnum) {
        this.menuName = menuName;
        this.menuInfo = menuInfo;
        this.menuPrice = menuPrice;
        this.menuImg = menuImg;
        this.menuPossibleEnum = menuPossibleEnum;
    }

    public MenuResponseDto(Menu menu) {
        this.menuName = menu.getMenuName();
        this.menuInfo = menu.getMenuInfo();
        this.menuPrice = String.valueOf(menu.getMenuPrice());
        this.menuImg = menu.getMenuImg();
        this.menuPossibleEnum = menu.getPossibleEnum();
    }
}
