package com.sparta.orderking.domain.auth.dto;

import com.sparta.orderking.domain.user.entity.UserEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthUser {
    private Long userId;
    private UserEnum userEnum;
}