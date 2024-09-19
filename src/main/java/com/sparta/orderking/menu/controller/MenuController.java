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

}
