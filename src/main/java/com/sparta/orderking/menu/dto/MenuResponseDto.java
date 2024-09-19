package com.sparta.orderking.menu.dto;

import com.sparta.orderking.menu.entity.MenuPossibleEnum;
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
}
