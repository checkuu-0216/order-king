package com.sparta.orderking.domain.user.dto.request;

import jakarta.validation.constraints.Email;
import lombok.Getter;

@Getter
public class LoginRequestDto {
    @Email(message = "이메일 형식이 아닙니다.")
    private String email;
    private String password;
}
