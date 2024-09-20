package com.sparta.orderking.domain.review.dto;

import lombok.Getter;

@Getter
public class UpdateReviewRequestDto {
    private String content;
    private int point;
}
