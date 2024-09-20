package com.sparta.orderking.domain.review.dto;

import com.sparta.orderking.domain.user.entity.User;

public class ReviewResponseDto {
    private User user;
    private String content;
    private int point;

    public ReviewResponseDto(User user, String content, int point) {
        this.user = user;
        this.content = content;
        this.point = point;
    }
}
