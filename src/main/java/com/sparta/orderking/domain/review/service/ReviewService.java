package com.sparta.orderking.domain.review.service;

import com.sparta.orderking.domain.order.entity.Order;
import com.sparta.orderking.domain.order.enums.OrderStatus;
import com.sparta.orderking.domain.order.repository.OrderRepository;
import com.sparta.orderking.domain.review.dto.CreateReviewRequestDto;
import com.sparta.orderking.domain.review.dto.ReviewResponseDto;
import com.sparta.orderking.domain.review.dto.UpdateReviewRequestDto;
import com.sparta.orderking.domain.review.entity.Review;
import com.sparta.orderking.domain.review.repository.ReviewRepository;
import com.sparta.orderking.domain.store.entity.Store;
import com.sparta.orderking.domain.store.repository.StoreRepository;
import com.sparta.orderking.domain.user.entity.User;
import com.sparta.orderking.domain.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;
    private final OrderRepository orderRepository;
    private final StoreRepository storeRepository;

    public ReviewResponseDto createReview(Long userId, Long storeId, Long orderId, CreateReviewRequestDto requestDto) {
        User user = userRepository.findById(userId).orElseThrow();
        Order order = orderRepository.findById(orderId).orElseThrow();
        Store store = storeRepository.findById(storeId).orElseThrow();

        // 배달 완료 되지 않으면 리뷰 작성 불가
        if(!order.getOrderStatus().equals(OrderStatus.DELIVERY_COMPLETED)) {
            throw new IllegalArgumentException("배달 완료된 주문만 리뷰 작성 가능합니다.");
        }

        Review review = new Review(user, order, store, requestDto.getContent(), requestDto.getPoint());
        reviewRepository.save(review);

        return new ReviewResponseDto(review.getUser(), review.getContent(), review.getPoint());
    }

    public ReviewResponseDto updateReview(Long userId, Long reviewId, UpdateReviewRequestDto requestDto) {
        User user = userRepository.findById(userId).orElseThrow();
        Review review = reviewRepository.findByIdAndUser(reviewId, user).orElseThrow(() -> new EntityNotFoundException("해당 리뷰가 존재하지 않습니다."));

        review.update(requestDto.getContent(), requestDto.getPoint());

        return new ReviewResponseDto(review.getUser(), review.getContent(), review.getPoint());
    }

    public void deleteReview(Long userId, Long reviewId) {
        User user = userRepository.findById(userId).orElseThrow();
        Review review = reviewRepository.findByIdAndUser(reviewId, user).orElseThrow(() -> new EntityNotFoundException("해당 리뷰가 존재하지 않습니다."));
        reviewRepository.delete(review);
    }

    public Page<ReviewResponseDto> getReviewsByPointRange(Long storeId, int minPoint, int maxPoint, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return reviewRepository.findAllByStoreIdAndPointBetweenOrderByCreatedAtDesc(storeId, minPoint, maxPoint, pageable);
    }
}
