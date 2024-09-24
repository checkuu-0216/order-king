package com.sparta.orderking.domain.store.service;

import com.sparta.orderking.domain.auth.dto.AuthUser;
import com.sparta.orderking.domain.review.entity.Review;
import com.sparta.orderking.domain.review.repository.ReviewRepository;
import com.sparta.orderking.domain.store.dto.request.OwnerReviewRequestDto;
import com.sparta.orderking.domain.store.dto.response.OwnerReviewResponseDto;
import com.sparta.orderking.domain.store.entity.OwnerReview;
import com.sparta.orderking.domain.store.entity.Store;
import com.sparta.orderking.domain.store.repository.OwnerReviewRepository;
import com.sparta.orderking.domain.user.entity.User;
import com.sparta.orderking.domain.user.service.UserService;
import com.sparta.orderking.exception.EntityNotFoundException;
import com.sparta.orderking.exception.WrongConditionException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OwnerReviewService {

    private final OwnerReviewRepository ownerReviewRepository;
    private final ReviewRepository reviewRepository;
    private final UserService userService;
    private final StoreService storeService;


    public Review findReview(long id) {
        return reviewRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("no such review"));
    }

    public void checkReview(Review review, Store store) {
        if (!review.getStore().equals(store)) {
            throw new WrongConditionException("review is not for the store");
        }
    }

    public OwnerReview findOwnerReview(long id) {
        return ownerReviewRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("no such owner review"));
    }

    public void lengthCheck(OwnerReviewRequestDto ownerReviewRequestDto) {
        if (ownerReviewRequestDto.getComment() == null ||
                ownerReviewRequestDto.getComment().length() > 255) {
            throw new WrongConditionException("write comment between 0 to 255");
        }
    }

    @Transactional
    public OwnerReviewResponseDto postComment(long storeId, long reviewId, AuthUser authUser, OwnerReviewRequestDto ownerReviewRequestDto) {
        User user = userService.findUser(authUser.getUserId());
        userService.checkAdmin(user);
        Store store = storeService.findStore(storeId);
        storeService.checkStoreOwner(store, user);
        Review review = findReview(reviewId);
        checkReview(review, store);
        lengthCheck(ownerReviewRequestDto);

        OwnerReview ownerReview = new OwnerReview(review, store, ownerReviewRequestDto.getComment());
        OwnerReview savedOwnerReview = ownerReviewRepository.save(ownerReview);
        return new OwnerReviewResponseDto(savedOwnerReview);
    }

    @Transactional
    public OwnerReviewResponseDto updateComment(long ownerReviewId, AuthUser authUser, OwnerReviewRequestDto ownerReviewRequestDto) {
        OwnerReview ownerReview = findOwnerReview(ownerReviewId);
        User user = userService.findUser(authUser.getUserId());
        userService.checkAdmin(user);
        Store store = storeService.findStore(ownerReview.getStore().getId());
        storeService.checkStoreOwner(store, user);
        lengthCheck(ownerReviewRequestDto);
        ownerReview.update(ownerReviewRequestDto.getComment());
        return new OwnerReviewResponseDto(ownerReview);
    }

    @Transactional
    public void deleteComment(long ownerReviewId, AuthUser authUser) {
        User user = userService.findUser(authUser.getUserId());
        userService.checkAdmin(user);
        OwnerReview ownerReview = findOwnerReview(ownerReviewId);
        Store store = storeService.findStore(ownerReview.getStore().getId());
        storeService.checkStoreOwner(store, user);
        ownerReviewRepository.delete(ownerReview);
    }
}
