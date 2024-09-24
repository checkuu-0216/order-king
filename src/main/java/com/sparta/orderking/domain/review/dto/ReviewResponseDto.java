package com.sparta.orderking.domain.review.dto;

import com.sparta.orderking.domain.review.entity.Review;
import com.sparta.orderking.domain.store.dto.response.OwnerReviewResponseDto;
import com.sparta.orderking.domain.user.entity.User;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class ReviewResponseDto {
    private Long userId;
    private String content;
    private int point;
    private List<OwnerReviewResponseDto> ownerReviewList;

    public ReviewResponseDto(User user, String content, int point) {
        this.userId = user.getId();
        this.content = content;
        this.point = point;
    }

    public ReviewResponseDto(Review review) {
        this.userId = review.getUser().getId();
        this.content = review.getContent();
        this.point = review.getPoint();
        this.ownerReviewList = review.getOwnerReviewList().stream()
                .map(OwnerReviewResponseDto::new)
                .collect(Collectors.toList());
    }
}
