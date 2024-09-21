package com.sparta.orderking.domain.store.dto.response;

import com.sparta.orderking.domain.store.entity.Bookmark;
import lombok.Getter;

@Getter
public class BookmarkSaveResponseDto {
    private final Long userId;
    private final Long storeId;

    public BookmarkSaveResponseDto(Bookmark bookmark) {
        this.userId = bookmark.getUser().getId();
        this.storeId = bookmark.getUser().getId();
    }
}
