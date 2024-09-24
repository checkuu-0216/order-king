package com.sparta.orderking.domain.review.controller;

import com.sparta.orderking.config.Auth;
import com.sparta.orderking.domain.auth.dto.AuthUser;
import com.sparta.orderking.domain.review.dto.CreateReviewRequestDto;
import com.sparta.orderking.domain.review.dto.ReviewResponseDto;
import com.sparta.orderking.domain.review.dto.UpdateReviewRequestDto;
import com.sparta.orderking.domain.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reviews")
public class ReviewController {
    private final ReviewService reviewService;

    // 리뷰 생성
    @PostMapping("/store/{storeId}/order/{orderId}")
    public ResponseEntity<ReviewResponseDto> createReview(@Auth AuthUser authUser,@PathVariable Long storeId, @PathVariable Long orderId, @RequestBody CreateReviewRequestDto requestDto) {
        return ResponseEntity.ok(reviewService.createReview(authUser.getUserId(), storeId, orderId, requestDto));
    }

    // 리뷰 수정
    @PutMapping("/{reviewId}")
    public ResponseEntity<ReviewResponseDto> updateReview(@Auth AuthUser authUser, @PathVariable Long reviewId, @RequestBody UpdateReviewRequestDto requestDto) {
        return ResponseEntity.ok(reviewService.updateReview(authUser.getUserId(), reviewId, requestDto));
    }

    // 리뷰 삭제
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<String> deleteReview(@Auth AuthUser authUser, @PathVariable Long reviewId) {
        reviewService.deleteReview(authUser.getUserId(), reviewId);
        return ResponseEntity.ok("성공적으로 삭제되었습니다.");
    }

    // 리뷰 조회
    @GetMapping("/store/{storeId}")
    public ResponseEntity<Page<ReviewResponseDto>> getReviews(
            @PathVariable Long storeId,
            @RequestParam(required = false) Integer minPoint,
            @RequestParam(required = false) Integer maxPoint,
            @RequestParam int page,
            @RequestParam int size
    ) {
        int defaultMinPoint = minPoint != null ? minPoint : 0;
        int defaultMaxPoint = maxPoint != null ? maxPoint : 5;

        Page<ReviewResponseDto> reviews = reviewService.getReviewsByPointRange(storeId, defaultMinPoint, defaultMaxPoint, page, size);
        return ResponseEntity.ok(reviews);
    }
}
