package com.sparta.orderking.domain.store.dto.response;

import com.sparta.orderking.domain.store.entity.OwnerReview;
import lombok.Getter;

@Getter
public class OwnerReviewResponseDto {
    private final Long reviewId;
    private final Long storeId;
    private final String comment;

    public OwnerReviewResponseDto(OwnerReview ownerReview) {
        this.reviewId = ownerReview.getReview().getId();
        this.storeId = ownerReview.getStore().getId();
        this.comment = ownerReview.getComment();
    }
}
