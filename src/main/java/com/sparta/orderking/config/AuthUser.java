package com.sparta.orderking.config;

import com.sparta.orderking.domain.user.entity.UserEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthUser {
    private Long id;
    private UserEnum userEnum;
}