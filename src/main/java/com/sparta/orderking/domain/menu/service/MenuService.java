package com.sparta.orderking.domain.menu.service;

import com.sparta.orderking.domain.menu.repository.MenuRepository;
import com.sparta.orderking.domain.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;
    private final StoreRepository storeRepository;

}
