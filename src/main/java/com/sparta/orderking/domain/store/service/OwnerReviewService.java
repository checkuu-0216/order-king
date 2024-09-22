package com.sparta.orderking.domain.store.service;

import com.sparta.orderking.config.AuthUser;
import com.sparta.orderking.domain.review.entity.Review;
import com.sparta.orderking.domain.review.repository.ReviewRepository;
import com.sparta.orderking.domain.store.dto.request.OwnerReviewRequestDto;
import com.sparta.orderking.domain.store.dto.response.OwnerReviewResponseDto;
import com.sparta.orderking.domain.store.entity.OwnerReview;
import com.sparta.orderking.domain.store.entity.Store;
import com.sparta.orderking.domain.store.repository.OwnerReviewRepository;
import com.sparta.orderking.domain.store.repository.StoreRepository;
import com.sparta.orderking.domain.user.entity.User;
import com.sparta.orderking.domain.user.entity.UserEnum;
import com.sparta.orderking.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OwnerReviewService {

    private final OwnerReviewRepository ownerReviewRepository;
    private final StoreRepository storeRepository;
    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;

    public void checkAdmin(AuthUser authUser) {
        if (!authUser.getUserEnum().equals(UserEnum.OWNER)) {
            throw new RuntimeException("owner only");
        }
    }

    public Store findStore(long storeId) {
        return storeRepository.findById(storeId).orElseThrow(() -> new NullPointerException("no such store"));
    }

    public void checkStoreOwner(Store store, User user) {
        if (!store.getUser().equals(user)) {
            throw new RuntimeException("you are not the owner of the store");
        }
    }

    public User findUser(long id) {
        return userRepository.findById(id).orElseThrow(() -> new NullPointerException("no such user"));
    }

    public Review findReview(long id) {
        return reviewRepository.findById(id).orElseThrow(() -> new NullPointerException("no such review"));
    }

    public void checkReview(Review review, Store store) {
        if (!review.getStore().equals(store)) {
            throw new RuntimeException("review is not for the store");
        }
    }

    public OwnerReview findOwnerReview(long id) {
        return ownerReviewRepository.findById(id).orElseThrow(() -> new NullPointerException("no such owner review"));
    }
    public void lengthCheck(OwnerReviewRequestDto ownerReviewRequestDto){
        if (ownerReviewRequestDto.getComment() == null ||
                ownerReviewRequestDto.getComment().length() > 255) {
            throw new RuntimeException("write comment between 0 to 255");
        }
    }
    @Transactional
    public OwnerReviewResponseDto postComment(long storeId, long reviewId, AuthUser authuser, OwnerReviewRequestDto ownerReviewRequestDto) {
        checkAdmin(authuser);
        Store store = findStore(storeId);
        User user = findUser(authuser.getId());
        checkStoreOwner(store, user);
        Review review = findReview(reviewId);
        checkReview(review, store);
        lengthCheck(ownerReviewRequestDto);

        OwnerReview ownerReview = new OwnerReview(review, store, ownerReviewRequestDto.getComment());
        OwnerReview savedOwnerReview = ownerReviewRepository.save(ownerReview);
        return new OwnerReviewResponseDto(savedOwnerReview);
    }

    @Transactional
    public OwnerReviewResponseDto updateComment(long ownerReviewId, AuthUser authUser, OwnerReviewRequestDto ownerReviewRequestDto) {
        checkAdmin(authUser);
        OwnerReview ownerReview = findOwnerReview(ownerReviewId);
        Store store = ownerReview.getStore();
        User user = findUser(authUser.getId());
        checkStoreOwner(store, user);
        lengthCheck(ownerReviewRequestDto);

        ownerReview.update(ownerReviewRequestDto.getComment());

        return new OwnerReviewResponseDto(ownerReview);
    }

    @Transactional
    public void deleteComment(long ownerReviewId, AuthUser authUser) {
        checkAdmin(authUser);
        OwnerReview ownerReview = findOwnerReview(ownerReviewId);
        Store store = ownerReview.getStore();
        User user = findUser(authUser.getId());
        checkStoreOwner(store, user);
        ownerReviewRepository.delete(ownerReview);
    }
}
