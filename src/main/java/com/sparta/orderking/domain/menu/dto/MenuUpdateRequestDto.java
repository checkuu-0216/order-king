package com.sparta.orderking.domain.menu.dto;

import com.sparta.orderking.domain.menu.entity.MenuPossibleEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MenuUpdateRequestDto {

    private String menuName;
    private String menuInfo;
    private int menuPrice;
    private String menuImg;
    private MenuPossibleEnum menuPossibleEnum;
}
