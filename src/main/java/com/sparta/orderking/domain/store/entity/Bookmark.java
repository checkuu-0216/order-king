package com.sparta.orderking.domain.store.entity;

import com.sparta.orderking.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Bookmark {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user")
    User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store")
    Store store;

    public Bookmark(User user, Store store) {
        this.user = user;
        this.store = store;
    }
}
