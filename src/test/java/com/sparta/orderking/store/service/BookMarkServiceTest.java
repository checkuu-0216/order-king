package com.sparta.orderking.store.service;

import com.sparta.orderking.domain.store.dto.response.BookmarkSaveResponseDto;
import com.sparta.orderking.domain.store.entity.Bookmark;
import com.sparta.orderking.domain.store.repository.BookmarkRepository;
import com.sparta.orderking.domain.store.repository.StoreRepository;
import com.sparta.orderking.domain.store.service.BookmarkService;
import com.sparta.orderking.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.sparta.orderking.store.CommonValue.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class BookMarkServiceTest {

    @Mock
    private BookmarkRepository bookmarkRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private StoreRepository storeRepository;
    @InjectMocks
    private BookmarkService bookmarkService;

    @Test
    void bookmarkOn_실패유저찾기(){
        Long storeId =1L;
        given(userRepository.findById(anyLong())).willReturn(Optional.empty());

        NullPointerException exception = assertThrows(NullPointerException.class,()->{
            bookmarkService.bookmarkOn(TEST_AUTHUSER,storeId);
        });

        assertEquals("there is no user",exception.getMessage());
    }

    @Test
    void bookmarkOn_실패가게찾기(){
        Long storeId =1L;

        given(userRepository.findById(anyLong())).willReturn(Optional.of(TEST_USER));
        given(storeRepository.findById(anyLong())).willReturn(Optional.empty());

        NullPointerException exception = assertThrows(NullPointerException.class,()->{
            bookmarkService.bookmarkOn(TEST_AUTHUSER,storeId);
        });
        assertEquals("there is no such store",exception.getMessage());
    }

    @Test
    void bookmarkOn_실패자기가게(){
        Long storeId=1L;
        given(userRepository.findById(anyLong())).willReturn(Optional.of(TEST_USER));
        given(storeRepository.findById(anyLong())).willReturn(Optional.of(TEST_STORE));

        RuntimeException exception = assertThrows(RuntimeException.class,()->{
            bookmarkService.bookmarkOn(TEST_AUTHUSER,storeId);
        });

        assertEquals("you can't bookmark your own store",exception.getMessage());
    }
    @Test
    void bookmarkOn_실패이미북마크(){
        Long storeId=1L;
        given(userRepository.findById(anyLong())).willReturn(Optional.of(TEST_USER));
        given(storeRepository.findById(anyLong())).willReturn(Optional.of(TEST_STORE4));
        given(bookmarkRepository.findByUserAndStore(any(),any())).willReturn(Optional.of(TEST_BOOKMARK));

        RuntimeException exception = assertThrows(RuntimeException.class,()->{
            bookmarkService.bookmarkOn(TEST_AUTHUSER,storeId);
        });

        assertEquals("you already bookmarked",exception.getMessage());
    }
    @Test
    void bookmarkOn(){
        Long storeId=1L;
        given(userRepository.findById(anyLong())).willReturn(Optional.of(TEST_USER));
        given(storeRepository.findById(anyLong())).willReturn(Optional.of(TEST_STORE4));
        given(bookmarkRepository.findByUserAndStore(any(),any())).willReturn(Optional.empty());
        given(bookmarkRepository.save(any())).willReturn(TEST_BOOKMARK);

        bookmarkService.bookmarkOn(TEST_AUTHUSER,storeId);

        verify(bookmarkRepository).save(any(Bookmark.class));
    }
    @Test
    void bookmarkOff(){
        Long storeId=1L;
        given(userRepository.findById(anyLong())).willReturn(Optional.of(TEST_USER));
        given(storeRepository.findById(anyLong())).willReturn(Optional.of(TEST_STORE4));
        given(bookmarkRepository.findByUserAndStore(any(),any())).willReturn(Optional.of(TEST_BOOKMARK));

        bookmarkService.bookmarkOff(TEST_AUTHUSER,storeId);

        verify(bookmarkRepository,times(1)).delete(any(Bookmark.class));
    }

}
