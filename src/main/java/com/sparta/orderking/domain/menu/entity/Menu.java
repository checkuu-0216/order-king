package com.sparta.orderking.domain.menu.entity;

import com.sparta.orderking.common.BaseEntity;
import com.sparta.orderking.domain.store.entity.Store;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Menu extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "menu_id")
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinColumn(name = "store")
    private Store store;

    @Column(name = "menuName")
    private String menuName;

    @Column(name = "menuPrice")
    private int menuPrice;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private MenuPossibleEnum possibleEnum;

}

