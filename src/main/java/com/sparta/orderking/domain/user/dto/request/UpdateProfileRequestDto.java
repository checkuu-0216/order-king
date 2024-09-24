package com.sparta.orderking.domain.user.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class UpdateProfileRequestDto {
    @Email(message = "이메일 형식이 아닙니다.")
    private String email;


    private String password;

    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "비밀번호는 최소 8자 이상, 영문 대소문자, 숫자, 특수문자를 포함해야 합니다."
    )
    private String newPassword;

    @NotBlank(message = "공백은 사용할 수 없습니다.")
    private String username;

    private String location;

}
