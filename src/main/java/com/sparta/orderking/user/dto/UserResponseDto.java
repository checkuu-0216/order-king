package com.sparta.orderking.user.dto;

import lombok.Getter;

@Getter
public class UserResponseDto {
    private String userName;
    private String userId;
    private String userAddress;
    private String userNumber;
    private String password;

    public UserResponseDto(String userName, String userId, String userAddress, String userNumber, String password) {
        this.userName = userName;
        this.userId = userId;
        this.userAddress = userAddress;
        this.userNumber = userNumber;
        this.password = password;
    }
}
