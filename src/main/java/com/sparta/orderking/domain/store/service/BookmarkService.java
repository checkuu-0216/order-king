package com.sparta.orderking.domain.store.service;

import com.sparta.orderking.domain.auth.dto.AuthUser;
import com.sparta.orderking.domain.store.dto.response.BookmarkSaveResponseDto;
import com.sparta.orderking.domain.store.entity.Bookmark;
import com.sparta.orderking.domain.store.entity.Store;
import com.sparta.orderking.domain.store.repository.BookmarkRepository;
import com.sparta.orderking.domain.user.entity.User;
import com.sparta.orderking.domain.user.service.UserService;
import com.sparta.orderking.exception.EntityNotFoundException;
import com.sparta.orderking.exception.WrongConditionException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final UserService userService;
    private final StoreService storeService;


    @Transactional
    public BookmarkSaveResponseDto bookmarkOn(AuthUser authUser, long storeId) {
        User user = userService.findUser(authUser.getUserId());
        Store store = storeService.findStore(storeId);
        if (store.getUser().equals(user)) {
            throw new WrongConditionException("you can't bookmark your own store");
        }
        if (bookmarkRepository.findByUserAndStore(user, store).isPresent()) {
            throw new WrongConditionException("you already bookmarked");
        }
        Bookmark bookmark = new Bookmark(user, store);
        Bookmark savedBookmark = bookmarkRepository.save(bookmark);

        return new BookmarkSaveResponseDto(savedBookmark);
    }

    @Transactional
    public void bookmarkOff(AuthUser authUser, long storeId) {
        User user = userService.findUser(authUser.getUserId());
        Store store = storeService.findStore(storeId);
        Bookmark bookmark = bookmarkRepository.findByUserAndStore(user, store).orElseThrow(() -> new EntityNotFoundException("you are not bookmark store"));
        bookmarkRepository.delete(bookmark);
    }
}
