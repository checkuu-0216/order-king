package com.sparta.orderking.domain.user.entity;

import java.util.Arrays;

public enum UserEnum {
    USER,
    OWNER;

    public static UserEnum of(String userEnum) {
        return Arrays.stream(UserEnum.values())
                .filter(r -> r.name().equalsIgnoreCase(userEnum))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("유효하지 않은 UserEnum"));
    }
}
