package com.sparta.orderking.domain.user.dto;

import lombok.Getter;

@Getter
public class LoginRequestDto {
    private String userName;
    private String userId;
    private String userAddress;
    private String userNumber;
    private String password;
}
