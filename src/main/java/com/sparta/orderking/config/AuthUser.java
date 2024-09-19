package com.sparta.orderking.config;

import com.sparta.orderking.user.entity.UserEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthUser {
    private Long id;
    private UserEnum userEnum;
}