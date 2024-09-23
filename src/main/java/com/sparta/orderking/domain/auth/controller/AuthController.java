package com.sparta.orderking.domain.auth.controller;

import com.sparta.orderking.domain.auth.service.AuthService;
import com.sparta.orderking.domain.user.dto.request.LoginRequestDto;
import com.sparta.orderking.domain.user.dto.request.SignupRequestDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@Valid @RequestBody SignupRequestDto requestDto) {
        String barerToken = authService.signup(requestDto);
        return ResponseEntity
                .ok()
                .header(HttpHeaders.AUTHORIZATION, barerToken)
                .build();
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody LoginRequestDto requestDto) {
        String barerToken = authService.login(requestDto);
        return ResponseEntity
                .ok()
                .header(HttpHeaders.AUTHORIZATION, barerToken)
                .build();
    }
}
