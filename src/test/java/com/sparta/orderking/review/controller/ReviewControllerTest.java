package com.sparta.orderking.review.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.orderking.config.AuthUserArgumentResolver;
import com.sparta.orderking.domain.review.controller.ReviewController;
import com.sparta.orderking.domain.review.dto.CreateReviewRequestDto;
import com.sparta.orderking.domain.review.dto.ReviewResponseDto;
import com.sparta.orderking.domain.review.service.ReviewService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static com.sparta.orderking.store.CommonValue.TEST_USER;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@MockBean(JpaMetamodelMappingContext.class)
@WebMvcTest(ReviewController.class)
public class ReviewControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReviewService reviewService;

    @Autowired
    private ObjectMapper objectMapper;

    @Mock
    private AuthUserArgumentResolver resolver;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(new ReviewController(reviewService))
                .setCustomArgumentResolvers(resolver)
                .build();
    }

    @Test
    void createReview() throws Exception {
        Long storeId = 1L;
        Long orderId = 1L;
        ReviewResponseDto dto = new ReviewResponseDto(TEST_USER, "content", 5);
        CreateReviewRequestDto requestDto = new CreateReviewRequestDto();

        given(reviewService.createReview(anyLong(), anyLong(), anyLong(), any())).willReturn(dto);

        ResultActions resultActions = mockMvc.perform(post("/api/reviews/store/{storeId}/order/{orderId}", storeId, orderId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)));

        resultActions.andExpect(status().isOk());
    }

    @Test
    void updateReview() throws Exception {
        Long reviewId = 1L;
        CreateReviewRequestDto requestDto = new CreateReviewRequestDto();
        ReviewResponseDto dto = new ReviewResponseDto(TEST_USER, "content", 5);

        given(reviewService.updateReview(anyLong(), anyLong(), any())).willReturn(dto);

        ResultActions resultActions = mockMvc.perform(put("/api/reviews/{reviewId}", reviewId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)));

        resultActions.andExpect(status().isOk());
    }

    @Test
    void deleteReview() throws Exception {
        Long reviewId = 1L;
        doNothing().when(reviewService).deleteReview(anyLong(), anyLong());

        ResultActions resultActions = mockMvc.perform(delete("/api/reviews/{reviewId}", reviewId));

        resultActions.andExpect(status().isOk());
    }
}


