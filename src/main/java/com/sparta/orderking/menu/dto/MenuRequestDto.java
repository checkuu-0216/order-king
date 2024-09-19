package com.sparta.orderking.menu.dto;

import com.sparta.orderking.menu.entity.MenuPossibleEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MenuRequestDto {

    private String menuName;
    private String menuInfo;
    private String menuPrice;
    private String menuImg;
    private MenuPossibleEnum menuPossibleEnum;

}
