package com.sparta.orderking.store.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.orderking.config.AuthUserArgumentResolver;
import com.sparta.orderking.domain.store.controller.BookMarkController;
import com.sparta.orderking.domain.store.dto.response.BookmarkSaveResponseDto;
import com.sparta.orderking.domain.store.entity.Bookmark;
import com.sparta.orderking.domain.store.service.BookmarkService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static com.sparta.orderking.store.CommonValue.TEST_STORE;
import static com.sparta.orderking.store.CommonValue.TEST_USER;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@MockBean(JpaMetamodelMappingContext.class)
@WebMvcTest(BookMarkController.class)
public class BookMarkControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookmarkService bookmarkService;

    @Mock
    private AuthUserArgumentResolver resolver;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(new BookMarkController(bookmarkService))
                .setCustomArgumentResolvers(resolver)
                .build();
    }

    @Test
    void 북마크생성() throws Exception {
        Long storeId = 1L;
        Bookmark bookmark = new Bookmark(TEST_USER, TEST_STORE);
        BookmarkSaveResponseDto dto = new BookmarkSaveResponseDto(bookmark);
        given(bookmarkService.bookmarkOn(any(), anyLong())).willReturn(dto);

        ResultActions resultActions = mockMvc.perform(post("/api/stores/{storeId}/bookmarkon", storeId));

        resultActions.andExpect(status().isOk());
    }

    @Test
    void 북마크삭제() throws Exception {
        Long storeId = 1L;
        doNothing().when(bookmarkService).bookmarkOff(any(), anyLong());

        ResultActions resultActions = mockMvc.perform(delete("/api/stores/{storeId}/bookmarkoff", storeId));

        resultActions.andExpect(status().isOk());
    }
}
