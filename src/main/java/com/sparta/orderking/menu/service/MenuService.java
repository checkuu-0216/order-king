package com.sparta.orderking.menu.service;

import com.sparta.orderking.config.AuthUser;
import com.sparta.orderking.domain.store.repository.StoreRepository;
import com.sparta.orderking.menu.dto.MenuRequestDto;
import com.sparta.orderking.menu.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;
    private final StoreRepository storeRepository;

}
