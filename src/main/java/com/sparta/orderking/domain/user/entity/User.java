package com.sparta.orderking.domain.user.entity;

import com.sparta.orderking.common.BaseEntity;
import com.sparta.orderking.domain.user.dto.request.SignupRequestDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class User extends BaseEntity {
    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "location")
    private String location;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted = false;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserEnum userEnum;

    public User(String username, String email, String password, String location,UserEnum userEnum) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.location = location;
        this.userEnum = userEnum;
    }

    public User(SignupRequestDto requestDto) {
        this.username = requestDto.getUsername();
        this.email = requestDto.getEmail();
        this.password = requestDto.getPassword();
        this.location = requestDto.getLocation();
        this.userEnum = requestDto.getUserEnum();
    }

    public User(UserEnum userEnum) {
        this.userEnum=userEnum;
    }

    public void update(User user) {
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.location = user.getLocation();
        this.userEnum = user.getUserEnum();
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
}
