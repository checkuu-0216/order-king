package com.sparta.orderking.domain.menu.dto;

import com.sparta.orderking.domain.menu.entity.Menu;
import com.sparta.orderking.domain.menu.entity.MenuCategoryEnum;
import com.sparta.orderking.domain.menu.entity.MenuPossibleEnum;
import lombok.Getter;

@Getter
public class MenuResponseDto {

    private final String menuName;
    private final String menuInfo;
    private final Integer menuPrice;
    private final String menuImg;
    private final MenuPossibleEnum menuPossibleEnum;
    private final MenuCategoryEnum menuCategoryEnum;

    public MenuResponseDto(String menuName, String menuInfo, int menuPrice, String menuImg, MenuPossibleEnum menuPossibleEnum, MenuCategoryEnum menuCategoryEnum) {
        this.menuName = menuName;
        this.menuInfo = menuInfo;
        this.menuPrice = menuPrice;
        this.menuImg = menuImg;
        this.menuPossibleEnum = menuPossibleEnum;
        this.menuCategoryEnum = menuCategoryEnum;
    }

    public MenuResponseDto(Menu menu) {
        this.menuName = menu.getMenuName();
        this.menuInfo = menu.getMenuInfo();
        this.menuPrice = menu.getMenuPrice();
        this.menuImg = menu.getMenuImg();
        this.menuPossibleEnum = menu.getMenuPossibleEnum();
        this.menuCategoryEnum = menu.getMenuCategoryEnum();
    }
}
