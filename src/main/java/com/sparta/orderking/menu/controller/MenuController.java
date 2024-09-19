package com.sparta.orderking.menu.controller;

import com.sparta.orderking.config.Auth;
import com.sparta.orderking.config.AuthUser;
import com.sparta.orderking.menu.dto.MenuRequestDto;
import com.sparta.orderking.menu.dto.MenuResponseDto;
import com.sparta.orderking.menu.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MenuController {

    private final MenuService menuService;

    @PostMapping("/stores/{storeId}/menus") //게시글 좋아요 생성
    public ResponseEntity<String> createMenu (@PathVariable Long storeId, @RequestBody MenuRequestDto requestDto){
        menuService.createMenu(storeId,requestDto);
        return ResponseEntity.ok().body("메뉴가 정상적으로 추가되었습니다.");
    }
}
