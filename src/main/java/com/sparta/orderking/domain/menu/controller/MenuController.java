package com.sparta.orderking.domain.menu.controller;

import com.sparta.orderking.config.Auth;
import com.sparta.orderking.domain.auth.dto.AuthUser;
import com.sparta.orderking.domain.menu.dto.MenuRequestDto;
import com.sparta.orderking.domain.menu.dto.MenuUpdateRequestDto;
import com.sparta.orderking.domain.menu.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MenuController {

    private final MenuService menuService;

    @PostMapping("/stores/{storeId}/menus")
    public ResponseEntity<String> saveMenu(@Auth AuthUser authUser, @PathVariable Long storeId, @RequestBody MenuRequestDto requestDto) {
        menuService.saveMenu(authUser, storeId, requestDto);
        return ResponseEntity.ok().body(HttpStatus.OK + ", menu save complete.");
    }

    @PutMapping("/stores/{storeId}/menus/{menuId}")
    public ResponseEntity<String> updateMenu(@Auth AuthUser authUser, @PathVariable Long storeId, @PathVariable Long menuId, @RequestBody MenuUpdateRequestDto requestDto) {
        menuService.updateMenu(authUser, storeId, menuId, requestDto);
        return ResponseEntity.ok().body(HttpStatus.OK + ", 메뉴 수정이 정상적으로 작동 하였습니다.");
    }

    @DeleteMapping("/stores/{storeId}/menus/{menuId}")
    public ResponseEntity<String> deleteMenu(@Auth AuthUser authUser, @PathVariable Long storeId, @PathVariable Long menuId) {
        menuService.deleteMenu(authUser, storeId, menuId);
        return ResponseEntity.ok().body(HttpStatus.OK + ", 메뉴 삭제가 정상적으로 작동 하였습니다.");
    }

}
