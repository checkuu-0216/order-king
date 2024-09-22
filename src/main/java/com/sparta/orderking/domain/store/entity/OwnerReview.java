package com.sparta.orderking.domain.store.entity;

import com.sparta.orderking.domain.review.entity.Review;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OwnerReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review")
    private Review review;

    @ManyToOne
    @JoinColumn(name = "store")
    private Store store;

    @Size(max = 255)
    @Column(name = "comment")
    private String comment;

    public OwnerReview(Review review, Store store, String comment) {
        this.review = review;
        this.store = store;
        this.comment = comment;
    }

    public void update(String comment) {
        this.comment = comment;
    }
}
