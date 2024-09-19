package com.sparta.orderking.domain.menu.service;

import com.sparta.orderking.config.AuthUser;
import com.sparta.orderking.domain.menu.dto.MenuRequestDto;
import com.sparta.orderking.domain.menu.dto.MenuUpdateRequestDto;
import com.sparta.orderking.domain.menu.entity.Menu;
import com.sparta.orderking.domain.menu.repository.MenuRepository;
import com.sparta.orderking.domain.store.entity.Store;
import com.sparta.orderking.domain.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;
    private final StoreRepository storeRepository;

    public void saveMenu(AuthUser authUser, Long storeId, MenuRequestDto requestDto) {
        //해당 가게 존재 확인
        Store store = storeRepository.findById(storeId).orElseThrow(() -> new NullPointerException("해당 가게가 존재하지 않습니다."));
        //해당 가게 주인이 맞는지 확인
        storeRepository.findByUserAndStore(authUser, store).orElseThrow(() -> new NullPointerException("해당 가게 주인이 아닙니다."));
        //등록할 메뉴 생성
        Menu menu = new Menu(requestDto);
        //메뉴 저장
        menuRepository.save(menu);
    }

    public void updateMenu(AuthUser authUser, Long storeId, Long menuId, MenuUpdateRequestDto requestDto) {
        //해당 가게 확인
        Store store = storeRepository.findById(storeId).orElseThrow(() -> new NullPointerException("해당 가게가 존재하지 않습니다."));
        //해당 가게 주인이 맞는지 확인
        storeRepository.findByUserAndStore(authUser, store).orElseThrow(() -> new NullPointerException("해당 가게 주인이 아닙니다."));
        //등록되어있는지 메뉴 확인
        Menu menu = menuRepository.findById(menuId).orElseThrow(() -> new NullPointerException("해당 메뉴가 존재 하지 않습니다."));
        //업데이트 된 request를 받아서 저장
        menu.updateMenu(requestDto);
        menuRepository.save(menu);
    }

    public void deleteMenu(AuthUser authUser, Long storeId, Long menuId) {
        //해당 가게 확인
        Store store = storeRepository.findById(storeId).orElseThrow(() -> new NullPointerException("해당 가게가 존재하지 않습니다."));
        //해당 가게 주인이 맞는지 확인
        storeRepository.findByUserAndStore(authUser, store).orElseThrow(() -> new NullPointerException("해당 가게 주인이 아닙니다."));
        //등록되어있는지 메뉴 확인
        Menu menu = menuRepository.findById(menuId).orElseThrow(() -> new NullPointerException("해당 메뉴가 존재 하지 않습니다."));
        //해당 메뉴 삭제
        menuRepository.delete(menu);
    }
}
