package com.sparta.orderking.review.Service;

import com.sparta.orderking.domain.order.repository.OrderRepository;
import com.sparta.orderking.domain.review.dto.CreateReviewRequestDto;
import com.sparta.orderking.domain.review.dto.ReviewResponseDto;
import com.sparta.orderking.domain.review.dto.UpdateReviewRequestDto;
import com.sparta.orderking.domain.review.entity.Review;
import com.sparta.orderking.domain.review.repository.ReviewRepository;
import com.sparta.orderking.domain.review.service.ReviewService;
import com.sparta.orderking.domain.store.service.StoreService;
import com.sparta.orderking.domain.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.sparta.orderking.store.CommonValue.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReviewServiceTest {
    @Mock
    private ReviewRepository reviewRepository;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private UserService userService;
    @Mock
    private StoreService storeService;
    @InjectMocks
    private ReviewService reviewService;

    @Test
    void createReview() {
        Long userId = 1L;
        Long storeId = 1L;
        Long orderId = 1L;
        CreateReviewRequestDto dto = new CreateReviewRequestDto();

        given(userService.findUser(userId)).willReturn(TEST_USER);
        given(orderRepository.findById(orderId)).willReturn(Optional.of(TEST_ORDER2));
        given(storeService.findStore(storeId)).willReturn(TEST_STORE);
        Review review = new Review(TEST_USER, TEST_ORDER2, TEST_STORE, "contents", 5);

        ReviewResponseDto response = reviewService.createReview(userId, storeId, orderId, dto);

        verify(reviewRepository, times(1)).save(argThat(reviews ->
                review.getUser().equals(TEST_USER) &&
                        review.getOrder().equals(TEST_ORDER2) &&
                        review.getStore().equals(TEST_STORE) &&
                        review.getContent().equals("contents") &&
                        review.getPoint() == 5
        ));
    }

    @Test
    void updateReview() {
        Long userId = 1L;
        Long reviewId = 1L;
        UpdateReviewRequestDto requestDto = new UpdateReviewRequestDto();
        ReflectionTestUtils.setField(requestDto, "content", "contents");
        ReflectionTestUtils.setField(requestDto, "point", 5);

        given(userService.findUser(userId)).willReturn(TEST_USER);
        given(reviewRepository.findByIdAndUser(anyLong(), any())).willReturn(Optional.of(TEST_REVIEW));

        ReviewResponseDto response = reviewService.updateReview(userId, reviewId, requestDto);

        assertNotNull(response);
    }

    @Test
    void deleteReview() {
        Long userId = 1L;
        Long reviewId = 1L;
        given(userService.findUser(userId)).willReturn(TEST_USER);
        given(reviewRepository.findByIdAndUser(anyLong(), any())).willReturn(Optional.of(TEST_REVIEW));

        doNothing().when(reviewRepository).delete(TEST_REVIEW);

        reviewService.deleteReview(userId, reviewId);

        verify(reviewRepository, times(1)).delete(TEST_REVIEW);
    }

    @Test
    void getReviewsByPointRange() {
        Long TEST_STORE_ID = 1L;
        int MIN_POINT = 3;
        int MAX_POINT = 5;
        int PAGE = 0;
        int SIZE = 10;

        Review review1 = new Review(TEST_USER, TEST_ORDER, TEST_STORE, "Great!", 5);
        Review review2 = new Review(TEST_USER, TEST_ORDER, TEST_STORE, "Good!", 4);
        List<Review> reviews = Arrays.asList(review1, review2);

        Page<Review> reviewPage = new PageImpl<>(reviews, PageRequest.of(PAGE, SIZE), reviews.size());

        given(reviewRepository.findAllByStoreIdAndPointBetweenOrderByCreatedAtDesc(
                anyLong(), anyInt(), anyInt(), any()))
                .willReturn(reviewPage);

        Page<ReviewResponseDto> result = reviewService.getReviewsByPointRange(TEST_STORE_ID, MIN_POINT, MAX_POINT, PAGE, SIZE);

        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertEquals(2, result.getContent().size());
        assertEquals("Great!", result.getContent().get(0).getContent());
        assertEquals("Good!", result.getContent().get(1).getContent());
    }

}
