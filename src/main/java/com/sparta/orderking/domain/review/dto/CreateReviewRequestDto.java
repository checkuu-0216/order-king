package com.sparta.orderking.domain.review.dto;

import lombok.Getter;

@Getter
public class CreateReviewRequestDto {
    private String content;
    private int point;
}
