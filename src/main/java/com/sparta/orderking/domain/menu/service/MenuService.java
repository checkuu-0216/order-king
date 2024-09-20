package com.sparta.orderking.domain.menu.service;

import com.sparta.orderking.config.AuthUser;
import com.sparta.orderking.domain.menu.dto.MenuRequestDto;
import com.sparta.orderking.domain.menu.dto.MenuUpdateRequestDto;
import com.sparta.orderking.domain.menu.entity.Menu;
import com.sparta.orderking.domain.menu.entity.MenuPossibleEnum;
import com.sparta.orderking.domain.menu.repository.MenuRepository;
import com.sparta.orderking.domain.store.entity.Store;
import com.sparta.orderking.domain.store.repository.StoreRepository;
import com.sparta.orderking.domain.user.entity.User;
import com.sparta.orderking.domain.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.sparta.orderking.domain.menu.entity.MenuPossibleEnum.DELETE;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MenuService {

    private final MenuRepository menuRepository;
    private final StoreRepository storeRepository;
    private final UserRepository userRepository;

    @Transactional
    public void saveMenu(AuthUser authUser, Long storeId, MenuRequestDto requestDto) {
        //해당 가게 존재 확인
        Store store = storeRepository.findById(storeId).orElseThrow(() -> new NullPointerException("해당 가게가 존재하지 않습니다."));
        //해당 가게 주인이 맞는지 확인
        User user = userRepository.findById(authUser.getId()).orElseThrow(() -> new EntityNotFoundException("해당 유저가 존재 하지 않습니다."));
//        storeRepository.findByStoreAndUser(user, store).orElseThrow(() -> new NullPointerException("해당 가게 주인이 아닙니다."));
        //등록할 메뉴 생성
        Menu menu = new Menu(requestDto);
        //메뉴 저장
        menuRepository.save(menu);
    }

    @Transactional
    public void updateMenu(AuthUser authUser, Long storeId, Long menuId, MenuUpdateRequestDto requestDto) {
        //해당 가게 확인
        Store store = storeRepository.findById(storeId).orElseThrow(() -> new NullPointerException("해당 가게가 존재하지 않습니다."));
        //해당 가게 주인이 맞는지 확인
        User user = userRepository.findById(authUser.getId()).orElseThrow(() -> new EntityNotFoundException("해당 유저가 존재 하지 않습니다."));
//        storeRepository.findByStoreAndUser(user, store).orElseThrow(() -> new NullPointerException("해당 가게 주인이 아닙니다."));
        //등록되어있는지 메뉴 확인
        Menu menu = menuRepository.findById(menuId).orElseThrow(() -> new NullPointerException("해당 메뉴가 존재 하지 않습니다."));
        //업데이트 된 request를 받아서 저장
        menu.updateMenu(requestDto);
        menuRepository.save(menu);
    }

    @Transactional
    public void deleteMenu(AuthUser authUser, Long storeId, Long menuId) {
        //해당 가게 확인
        Store store = storeRepository.findById(storeId).orElseThrow(() -> new NullPointerException("해당 가게가 존재하지 않습니다."));
        //해당 가게 주인이 맞는지 확인
        User user = userRepository.findById(authUser.getId()).orElseThrow(() -> new EntityNotFoundException("해당 유저가 존재 하지 않습니다."));
//        storeRepository.findByStoreAndUser(user, store).orElseThrow(() -> new NullPointerException("해당 가게 주인이 아닙니다."));
        //등록되어있는지 메뉴 확인
        Menu menu = menuRepository.findById(menuId).orElseThrow(()->new NullPointerException("해당 메뉴가 존재 하지 않습니다."));
        menu.deleteMenu(DELETE);
        //등록되어있는 메뉴 상태 변화
        menuRepository.save(menu);
    }
}
