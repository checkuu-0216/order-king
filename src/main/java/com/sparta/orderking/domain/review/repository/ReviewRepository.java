package com.sparta.orderking.domain.review.repository;

import com.sparta.orderking.domain.review.entity.Review;
import com.sparta.orderking.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    Optional<Review> findByIdAndUser(Long reviewId, User user);

    Page<Review> findAllByStoreIdAndPointBetweenOrderByCreatedAtDesc(Long storeId, int minPoint, int maxPoint, Pageable pageable);
}
