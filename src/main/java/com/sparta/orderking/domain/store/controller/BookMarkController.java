package com.sparta.orderking.domain.store.controller;

import com.sparta.orderking.config.Auth;
import com.sparta.orderking.config.AuthUser;
import com.sparta.orderking.domain.store.dto.BookmarkSaveResponseDto;
import com.sparta.orderking.domain.store.repository.BookmarkRepository;
import com.sparta.orderking.domain.store.service.BookmarkService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/stores")
public class BookMarkController {

    private final BookmarkService bookmarkService;

    @PostMapping("/stores/{storeId}/bookmarkon")
    public ResponseEntity<BookmarkSaveResponseDto> bookmarkOn(@Auth AuthUser authUser, @PathVariable long storeId){
        return ResponseEntity.ok(bookmarkService.bookmarkOn(authUser,storeId));
    }

    @DeleteMapping("/stores/{storeId}/bookmarkoff")
    public void bookmarkOff(@Auth AuthUser authUser, @PathVariable long storeId){
        bookmarkService.bookmarkOff(authUser,storeId);
    }
}
