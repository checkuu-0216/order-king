package com.sparta.orderking.store.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.orderking.config.AuthUserArgumentResolver;
import com.sparta.orderking.domain.store.controller.BookMarkController;
import com.sparta.orderking.domain.store.controller.OwnerReviewController;
import com.sparta.orderking.domain.store.dto.request.OwnerReviewRequestDto;
import com.sparta.orderking.domain.store.dto.response.OwnerReviewResponseDto;
import com.sparta.orderking.domain.store.entity.OwnerReview;
import com.sparta.orderking.domain.store.service.BookmarkService;
import com.sparta.orderking.domain.store.service.OwnerReviewService;
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

import static com.sparta.orderking.store.CommonValue.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@MockBean(JpaMetamodelMappingContext.class)
@WebMvcTest(OwnerReviewController.class)
public class OwnerReviewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OwnerReviewService ownerReviewService;

    @Mock
    private AuthUserArgumentResolver resolver;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(new OwnerReviewController(ownerReviewService))
                .setCustomArgumentResolvers(resolver)
                .build();
    }

    @Test
    void 사장님댓글생성() throws Exception {
        Long storeId = 1L;
        Long reviewId = 1L;
        OwnerReview ownerReview = new OwnerReview(TEST_REVIEW,TEST_STORE,"comment");
        OwnerReviewResponseDto dto = new OwnerReviewResponseDto(ownerReview);
        OwnerReviewRequestDto requestDto = new OwnerReviewRequestDto();

        given(ownerReviewService.postComment(anyLong(),anyLong(),any(),any())).willReturn(dto);

        ResultActions resultActions = mockMvc.perform(post("/api/stores/{storeId}/review/{reviewId}/ownerReview"
                ,storeId,reviewId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)));

        resultActions.andExpect(status().isOk());
    }
    @Test
    void 사장님댓글수정() throws Exception{
        Long ownerReviewId =1L;
        OwnerReviewRequestDto requestDto = new OwnerReviewRequestDto();
        OwnerReview ownerReview = new OwnerReview(TEST_REVIEW,TEST_STORE,"comment");
        OwnerReviewResponseDto dto = new OwnerReviewResponseDto(ownerReview);

        given(ownerReviewService.updateComment(anyLong(),any(),any())).willReturn(dto);

        ResultActions resultActions = mockMvc.perform(put("/api/stores/ownerReview/{ownerReviewId}",ownerReviewId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)));

        resultActions.andExpect(status().isOk());
    }
    @Test
    void 사장님댓글삭제() throws Exception {
        Long ownerReviewId =1L;
        doNothing().when(ownerReviewService).deleteComment(anyLong(),any());

        ResultActions resultActions = mockMvc.perform(delete("/api/stores/ownerReview/{ownerReviewId}",ownerReviewId));

        resultActions.andExpect(status().isOk());
    }


}
