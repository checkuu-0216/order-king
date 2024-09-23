package com.sparta.orderking.store.service;

import com.sparta.orderking.domain.review.entity.Review;
import com.sparta.orderking.domain.review.repository.ReviewRepository;
import com.sparta.orderking.domain.store.dto.request.OwnerReviewRequestDto;
import com.sparta.orderking.domain.store.dto.response.OwnerReviewResponseDto;
import com.sparta.orderking.domain.store.entity.OwnerReview;
import com.sparta.orderking.domain.store.entity.Store;
import com.sparta.orderking.domain.store.repository.OwnerReviewRepository;
import com.sparta.orderking.domain.store.repository.StoreRepository;
import com.sparta.orderking.domain.store.service.OwnerReviewService;
import com.sparta.orderking.domain.user.entity.User;
import com.sparta.orderking.domain.user.entity.UserEnum;
import com.sparta.orderking.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static com.sparta.orderking.store.CommonValue.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OwnerReviewServiceTest {

    @Mock
    private OwnerReviewRepository ownerReviewRepository;
    @Mock
    private StoreRepository storeRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ReviewRepository reviewRepository;
    @InjectMocks
    private OwnerReviewService ownerReviewService;

    @Test
    void checkAdmin(){
        RuntimeException exception = assertThrows(RuntimeException.class,()->{
            ownerReviewService.checkAdmin(TEST_AUTHUSER2);
        });
        assertEquals("owner only",exception.getMessage());
    }
    @Test
    void findStore(){
        Long storeId=1L;
        given(storeRepository.findById(storeId)).willReturn(Optional.empty());

        NullPointerException exception = assertThrows(NullPointerException.class, ()->{
            ownerReviewService.findStore(storeId);
        });
        assertEquals("no such store",exception.getMessage());
    }

    @Test
    void checkStoreOwner(){
        Store store = TEST_STORE;
        User user = TEST_USER2;
        RuntimeException exception = assertThrows(RuntimeException.class,()->{
            ownerReviewService.checkStoreOwner(store,user);
        });

        assertEquals("you are not the owner of the store",exception.getMessage());
    }
    @Test
    void findUser(){
        Long id = 2L;
        User user = new User(UserEnum.USER);
        ReflectionTestUtils.setField(user,"id",1L);
        NullPointerException exception = assertThrows(NullPointerException.class,()->{
            ownerReviewService.findUser(id);
        });

        assertEquals("no such user",exception.getMessage());
    }
    @Test
    void findReview(){
        Long id =1L;
        given(reviewRepository.findById(anyLong())).willReturn(Optional.empty());

        NullPointerException exception = assertThrows(NullPointerException.class,()->{
            ownerReviewService.findReview(id);
        });

        assertEquals("no such review",exception.getMessage());
    }
    @Test
    void checkReview(){
        Review review = TEST_REVIEW;
        Store store = TEST_STORE4;

        RuntimeException exception = assertThrows(RuntimeException.class,()->{
            ownerReviewService.checkReview(review,store);
        });

        assertEquals("review is not for the store",exception.getMessage());
    }
    @Test
    void findOwnerReview(){
        Long id =1L;
        given(ownerReviewRepository.findById(anyLong())).willReturn(Optional.empty());

        NullPointerException exception = assertThrows(NullPointerException.class,()->{
            ownerReviewService.findOwnerReview(id);
        });

        assertEquals("no such owner review",exception.getMessage());
    }
    @Test
    void lengthCheck(){
        OwnerReviewRequestDto dto =new OwnerReviewRequestDto();

        RuntimeException exception = assertThrows(RuntimeException.class,()->{
            ownerReviewService.lengthCheck(dto);
        });

        assertEquals("write comment between 0 to 255",exception.getMessage());
    }

    @Test
    void postComment(){
        Long storeId = 1L;
        Long reviewId = 1L;
        OwnerReviewRequestDto dto = new OwnerReviewRequestDto("이것은 댓글입니다.");

        ReflectionTestUtils.setField(TEST_STORE,"user",TEST_USER);
        ReflectionTestUtils.setField(TEST_REVIEW,"store",TEST_STORE);

        given(storeRepository.findById(storeId)).willReturn(Optional.of(TEST_STORE));
        given(userRepository.findById(TEST_AUTHUSER.getUserId())).willReturn(Optional.of(TEST_USER));
        given(reviewRepository.findById(reviewId)).willReturn(Optional.of(TEST_REVIEW));
        given(ownerReviewRepository.save(any(OwnerReview.class))).willAnswer(invocation -> invocation.getArgument(0));

        OwnerReviewResponseDto response = ownerReviewService.postComment(storeId, reviewId, TEST_AUTHUSER, dto);

        assertNotNull(response);
        verify(ownerReviewRepository).save(any(OwnerReview.class));
    }
    @Test
    void updateComment(){
        Long ownerReviewId =1L;
        OwnerReviewRequestDto dto = new OwnerReviewRequestDto("이것은 댓글입니다.");
        OwnerReview ownerReview = new OwnerReview(TEST_REVIEW,TEST_STORE,"1234");

        given(userRepository.findById(anyLong())).willReturn(Optional.of(TEST_USER));
        given(ownerReviewRepository.findById(anyLong())).willReturn(Optional.of(ownerReview));

        OwnerReviewResponseDto dto1 = ownerReviewService.updateComment(ownerReviewId,TEST_AUTHUSER,dto);

        assertEquals(dto1.getComment(),dto.getComment());
    }

    @Test
    void deleteComment(){
        Long ownerReviewId =1L;
        OwnerReview ownerReview = new OwnerReview(TEST_REVIEW,TEST_STORE,"1234");

        given(userRepository.findById(anyLong())).willReturn(Optional.of(TEST_USER));
        given(ownerReviewRepository.findById(anyLong())).willReturn(Optional.of(ownerReview));
        doNothing().when(ownerReviewRepository).delete(any());

        ownerReviewService.deleteComment(ownerReviewId,TEST_AUTHUSER);

        verify(ownerReviewRepository).delete(any(OwnerReview.class));
    }

}
