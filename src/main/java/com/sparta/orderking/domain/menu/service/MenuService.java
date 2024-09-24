package com.sparta.orderking.domain.menu.service;

import com.sparta.orderking.domain.auth.dto.AuthUser;
import com.sparta.orderking.domain.menu.dto.MenuRequestDto;
import com.sparta.orderking.domain.menu.dto.MenuUpdateRequestDto;
import com.sparta.orderking.domain.menu.entity.Menu;
import com.sparta.orderking.domain.menu.repository.MenuRepository;
import com.sparta.orderking.domain.store.entity.Store;
import com.sparta.orderking.domain.store.repository.StoreRepository;
import com.sparta.orderking.domain.user.entity.User;
import com.sparta.orderking.domain.user.repository.UserRepository;
import com.sparta.orderking.exception.EntityAlreadyExistsException;
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
        //가게 주인 확인 메서드
        Store store = validateStoreOwner(authUser,storeId);
        //메뉴 중복 생성 방지
        if(menuRepository.existsByStoreAndMenuName(store,requestDto.getMenuName())){
            throw new EntityAlreadyExistsException("이미 존재하는 메뉴 입니다."); //커스텀
        }
        //등록할 메뉴 생성
        Menu menu = new Menu(requestDto,store);
        //메뉴 저장
        menuRepository.save(menu);
    }

    @Transactional
    public void updateMenu(AuthUser authUser, Long storeId, Long menuId, MenuUpdateRequestDto requestDto) {
        Store store = validateStoreOwner(authUser,storeId);
        //등록되어있는지 메뉴 확인
        Menu menu = menuRepository.findById(menuId).orElseThrow(() -> new NullPointerException("해당 메뉴가 존재 하지 않습니다.")); //커스텀 exception or notfindexection
        //업데이트 된 request를 받아서 저장
        menu.updateMenu(requestDto,store);
        menuRepository.save(menu); //transactional 있어서 save 안해도 자동으로 저장된다.
    }

    @Transactional
    public void deleteMenu(AuthUser authUser, Long storeId, Long menuId) {
        Store store = validateStoreOwner(authUser,storeId);
        //등록되어있는지 메뉴 확인
        Menu menu = menuRepository.findById(menuId).orElseThrow(()->new NullPointerException("해당 메뉴가 존재 하지 않습니다."));
        menu.deleteMenu(DELETE);
        //등록되어있는 메뉴 상태 변화
        menuRepository.save(menu);
    }

    private Store validateStoreOwner(AuthUser authUser, Long storeId) {
        // 가게 존재 확인
        Store store = storeRepository.findById(storeId).orElseThrow(() -> new NullPointerException("해당 가게가 존재하지 않습니다."));
        // 가게 주인 확인
        User user = userRepository.findById(authUser.getUserId()).orElseThrow(() -> new EntityNotFoundException("해당 유저가 존재하지 않습니다.")); //user에서 한번에 처리
        // 가게의 주인과 유저가 일치하는지 확인
        if (!user.getId().equals(store.getUser().getId())) {
            throw new NullPointerException("해당 가게의 주인이 아닙니다."); // 커스텀 exception
        }
        return store;
    }
}

