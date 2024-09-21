package com.sparta.orderking.domain.store.repository;

import com.sparta.orderking.domain.store.entity.Bookmark;
import com.sparta.orderking.domain.store.entity.Store;
import com.sparta.orderking.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookmarkRepository extends JpaRepository<Bookmark,Long> {
    Optional<Bookmark> findByUserAndStore(User user, Store store);
}
