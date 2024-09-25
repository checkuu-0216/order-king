package com.sparta.orderking.domain.user.dto.request;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class DeleteUserRequestDto {
    @Email(message = "이메일 형식이 아닙니다.")
    private String email;

    private String password;
}
