package com.sparta.orderking.domain.store.controller;

import com.sparta.orderking.config.Auth;
import com.sparta.orderking.domain.auth.dto.AuthUser;
import com.sparta.orderking.domain.store.dto.request.OwnerReviewRequestDto;
import com.sparta.orderking.domain.store.dto.response.OwnerReviewResponseDto;
import com.sparta.orderking.domain.store.service.OwnerReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/stores")
public class OwnerReviewController {

    private final OwnerReviewService ownerReviewService;

    @PostMapping("/{storeId}/review/{reviewId}/ownerReview")
    public ResponseEntity<OwnerReviewResponseDto> postComment(@PathVariable long storeId, @PathVariable long reviewId, @Auth AuthUser authuser,
                                                              @RequestBody OwnerReviewRequestDto ownerReviewRequestDto) {
        return ResponseEntity.ok(ownerReviewService.postComment(storeId, reviewId, authuser, ownerReviewRequestDto));
    }

    @PutMapping("/ownerReview/{ownerReviewId}")
    public ResponseEntity<OwnerReviewResponseDto> updateComment(@PathVariable long ownerReviewId, @Auth AuthUser authuser,
                                                                @RequestBody OwnerReviewRequestDto ownerReviewRequestDto) {
        return ResponseEntity.ok(ownerReviewService.updateComment(ownerReviewId, authuser, ownerReviewRequestDto));
    }

    @DeleteMapping("ownerReview/{ownerReviewId}")
    public void deleteComment(@PathVariable long ownerReviewId, @Auth AuthUser authUser) {
        ownerReviewService.deleteComment(ownerReviewId, authUser);
    }
}
