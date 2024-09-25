package com.sparta.orderking.domain.user.dto.response;

import com.sparta.orderking.domain.user.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class UserResponseDto {
    private Long id;

    private String email;

    private String username;

    private String imgUrl;

    private String location;

    private Integer age;

    public UserResponseDto(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.username = user.getUsername();
        this.location = user.getLocation();
    }
}
