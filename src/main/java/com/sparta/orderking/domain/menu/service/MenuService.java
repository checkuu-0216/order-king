package com.sparta.orderking.domain.menu.service;

import com.sparta.orderking.domain.auth.dto.AuthUser;
import com.sparta.orderking.domain.menu.dto.MenuRequestDto;
import com.sparta.orderking.domain.menu.dto.MenuUpdateRequestDto;
import com.sparta.orderking.domain.menu.entity.Menu;
import com.sparta.orderking.domain.menu.repository.MenuRepository;
import com.sparta.orderking.domain.store.entity.Store;
import com.sparta.orderking.domain.store.service.StoreService;
import com.sparta.orderking.domain.user.entity.User;
import com.sparta.orderking.domain.user.service.UserService;
import com.sparta.orderking.exception.EntityAlreadyExistsException;
import com.sparta.orderking.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.sparta.orderking.domain.menu.entity.MenuPossibleEnum.DELETE;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MenuService {

    private final MenuRepository menuRepository;
    private final StoreService storeService;
    private final UserService userService;

    @Transactional
    public void saveMenu(AuthUser authUser, Long storeId, MenuRequestDto requestDto) {
        //가게 주인 확인 메서드
        User user = userService.findUser(authUser.getUserId());
        Store store = storeService.findStore(storeId);
        storeService.checkStoreOwner(store,user);
        //메뉴 중복 생성 방지
        if(menuRepository.existsByStoreAndMenuName(store,requestDto.getMenuName())){
            throw new EntityAlreadyExistsException("이미 존재하는 메뉴 입니다.");
        }
        //등록할 메뉴 생성
        Menu menu = new Menu(requestDto,store);
        //메뉴 저장
        menuRepository.save(menu);
    }

    @Transactional
    public void updateMenu(AuthUser authUser, Long storeId, Long menuId, MenuUpdateRequestDto requestDto) {
        //가게 주인 확인 메서드
        User user = userService.findUser(authUser.getUserId());
        Store store = storeService.findStore(storeId);
        storeService.checkStoreOwner(store,user);
        //등록되어있는지 메뉴 확인
        Menu menu = menuRepository.findById(menuId).orElseThrow(() -> new EntityNotFoundException("해당 메뉴가 존재 하지 않습니다."));
        //업데이트 된 request를 받아서 저장
        menu.updateMenu(requestDto,store);
    }

    @Transactional
    public void deleteMenu(AuthUser authUser, Long storeId, Long menuId) {
        //가게 주인 확인 메서드
        User user = userService.findUser(authUser.getUserId());
        Store store = storeService.findStore(storeId);
        storeService.checkStoreOwner(store,user);
        //등록되어있는지 메뉴 확인
        Menu menu = menuRepository.findById(menuId).orElseThrow(()->new EntityNotFoundException("해당 메뉴가 존재 하지 않습니다."));
        //등록되어있는 메뉴 상태 변화
        menu.deleteMenu(DELETE);
    }
}

