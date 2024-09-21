package com.sparta.orderking.domain.store.service;

import com.sparta.orderking.config.AuthUser;
import com.sparta.orderking.domain.store.dto.response.BookmarkSaveResponseDto;
import com.sparta.orderking.domain.store.entity.Bookmark;
import com.sparta.orderking.domain.store.entity.Store;
import com.sparta.orderking.domain.store.repository.BookmarkRepository;
import com.sparta.orderking.domain.store.repository.StoreRepository;
import com.sparta.orderking.domain.user.entity.User;
import com.sparta.orderking.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final UserRepository userRepository;
    private final StoreRepository storeRepository;

    public BookmarkSaveResponseDto bookmarkOn(AuthUser authUser, long storeId) {
        User user = userRepository.findById(authUser.getId()).orElseThrow(()->new NullPointerException("there is no user"));
        Store store = storeRepository.findById(storeId).orElseThrow(()->new NullPointerException("there is no such store"));
        if(store.getUser().equals(user)){
            throw new RuntimeException("you can't bookmark your own store");
        }
        if(bookmarkRepository.findByUserAndStore(user,store).isPresent()){
            throw new RuntimeException("you already bookmarked");
        }
        Bookmark bookmark = new Bookmark(user,store);
        Bookmark savedBookmark = bookmarkRepository.save(bookmark);

        return new BookmarkSaveResponseDto(savedBookmark);
    }

    public void bookmarkOff(AuthUser authUser, long storeId) {
        User user = userRepository.findById(authUser.getId()).orElseThrow(()->new NullPointerException("there is no user"));
        Store store = storeRepository.findById(storeId).orElseThrow(()->new NullPointerException("there is no such store"));
        Bookmark bookmark = bookmarkRepository.findByUserAndStore(user,store).orElseThrow(()-> new NullPointerException("you are not bookmark store"));
        bookmarkRepository.delete(bookmark);
    }
}
