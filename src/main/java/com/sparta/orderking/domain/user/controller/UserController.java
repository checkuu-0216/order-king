package com.sparta.orderking.domain.user.controller;

import com.sparta.orderking.config.Auth;
import com.sparta.orderking.domain.auth.dto.AuthUser;
import com.sparta.orderking.domain.user.dto.request.DeleteUserRequestDto;
import com.sparta.orderking.domain.user.dto.request.UpdateProfileRequestDto;
import com.sparta.orderking.domain.user.dto.response.UserResponseDto;
import com.sparta.orderking.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController()
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    // 유저 프로필 조회
    @GetMapping("")
    public ResponseEntity<UserResponseDto> getUserProfile(@Auth AuthUser authUser) {
        return ResponseEntity.ok(userService.getUser(authUser));
    }

    // 유저 프로필 업데이트
    @PutMapping("")
    public ResponseEntity<String> updateUserProfile(@Auth AuthUser authUser,
                                                    @Valid @RequestBody UpdateProfileRequestDto requestDto) {
        userService.updateUserProfile(authUser, requestDto);
        return ResponseEntity.ok().body("업데이트가 완료되었습니다.");
    }

    // 유저 탈퇴
    @DeleteMapping("")
    public ResponseEntity<String> deleteUser(@Auth AuthUser authUser, @Valid @RequestBody DeleteUserRequestDto requestDto) {
        userService.deleteUser(authUser, requestDto);
        return ResponseEntity.ok().body("유저가 탈퇴 되었습니다.");
    }

}
