package com.sparta.orderking.menu.service;

import com.sparta.orderking.domain.auth.dto.AuthUser;
import com.sparta.orderking.domain.menu.dto.MenuRequestDto;
import com.sparta.orderking.domain.menu.dto.MenuUpdateRequestDto;
import com.sparta.orderking.domain.menu.entity.Menu;
import com.sparta.orderking.domain.menu.entity.MenuCategoryEnum;
import com.sparta.orderking.domain.menu.entity.MenuPossibleEnum;
import com.sparta.orderking.domain.menu.repository.MenuRepository;
import com.sparta.orderking.domain.menu.service.MenuService;
import com.sparta.orderking.domain.store.entity.Store;
import com.sparta.orderking.domain.store.service.StoreService;
import com.sparta.orderking.domain.user.entity.User;
import com.sparta.orderking.domain.user.entity.UserEnum;
import com.sparta.orderking.domain.user.service.UserService;
import com.sparta.orderking.exception.EntityAlreadyExistsException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class MenuServiceTest {
    @Mock
    private MenuRepository menuRepository;

    @Mock
    private StoreService storeService;

    @Mock
    private UserService userService;

    @InjectMocks
    private MenuService menuService;

    @Test
    public void 메뉴_중복_등록방지_에러_작동() {
        //given
        long storeId = 1L;
        AuthUser authUser = new AuthUser(1L, UserEnum.OWNER);
        User user = userService.findUser(1L);
        Store store = storeService.findStore(storeId);
        storeService.checkStoreOwner(store, user);
        MenuRequestDto requestDto = new MenuRequestDto("a", "a", 1000, "a", MenuPossibleEnum.SALE, MenuCategoryEnum.KOREAN);
        Menu menu = new Menu(requestDto, store);
        given(menuRepository.existsByStoreAndMenuName(any(), any())).willReturn(true);
        //when
        EntityAlreadyExistsException exception = assertThrows(EntityAlreadyExistsException.class, () -> menuService.saveMenu(authUser, storeId, requestDto));
        //then
        assertEquals("이미 존재하는 메뉴 입니다.", exception.getMessage());
    }

    @Test
    public void 메뉴_등록_정상작동() {
        //given
        long storeId = 1L;
        AuthUser authUser = new AuthUser(1L, UserEnum.OWNER);
        User user = userService.findUser(1L);
        Store store = storeService.findStore(storeId);
        storeService.checkStoreOwner(store, user);
        MenuRequestDto requestDto = new MenuRequestDto("a", "a", 1000, "a", MenuPossibleEnum.SALE, MenuCategoryEnum.KOREAN);
        Menu menu = new Menu(requestDto, store);

        given(menuRepository.existsByStoreAndMenuName(any(), any())).willReturn(false);
        //when
        menuService.saveMenu(authUser, storeId, requestDto);
        //then
        assertNotNull(menu);
        assertEquals(menu.getMenuName(), requestDto.getMenuName());
        assertEquals(menu.getMenuInfo(), requestDto.getMenuInfo());
        assertEquals(menu.getMenuPrice(), requestDto.getMenuPrice());
        assertEquals(menu.getMenuPrice(), requestDto.getMenuPrice());
        assertEquals(menu.getMenuPossibleEnum(), requestDto.getMenuPossibleEnum());
        assertEquals(menu.getMenuCategoryEnum(), requestDto.getMenuCategoryEnum());
    }

    @Test
    public void 메뉴_수정_정상작동() {
        //given
        long storeId = 1L;
        AuthUser authUser = new AuthUser(1L, UserEnum.OWNER);
        User user = userService.findUser(1L);
        Store store = storeService.findStore(storeId);
        storeService.checkStoreOwner(store, user);
        long menuId = 1L;
        MenuRequestDto requestDto = new MenuRequestDto("b", "b", 1000, "a", MenuPossibleEnum.SALE, MenuCategoryEnum.KOREAN);
        MenuUpdateRequestDto requestDto1 = new MenuUpdateRequestDto("a", "a", 1000, "a", MenuPossibleEnum.SALE, MenuCategoryEnum.KOREAN);
        Menu menu = new Menu(requestDto, store);
        given(menuRepository.findById(anyLong())).willReturn(Optional.of(menu));
        //when
        menuService.updateMenu(authUser, storeId, menuId, requestDto1);
        //then
        assertNotNull(menu);
        assertEquals(menu.getMenuName(), requestDto1.getMenuName());
        assertEquals(menu.getMenuInfo(), requestDto1.getMenuInfo());
        assertEquals(menu.getMenuPrice(), requestDto1.getMenuPrice());
        assertEquals(menu.getMenuPrice(), requestDto1.getMenuPrice());
        assertEquals(menu.getMenuPossibleEnum(), requestDto1.getMenuPossibleEnum());
        assertEquals(menu.getMenuCategoryEnum(), requestDto1.getMenuCategoryEnum());
    }

    @Test
    public void 메뉴_삭제_정상작동() {
        //given
        long storeId = 1L;
        AuthUser authUser = new AuthUser(1L, UserEnum.OWNER);
        User user = userService.findUser(1L);
        Store store = storeService.findStore(storeId);
        storeService.checkStoreOwner(store, user);
        long menuId = 1L;
        MenuRequestDto requestDto = new MenuRequestDto("b", "b", 1000, "a", MenuPossibleEnum.SALE, MenuCategoryEnum.KOREAN);
        Menu menu = new Menu(requestDto, store);
        given(menuRepository.findById(anyLong())).willReturn(Optional.of(menu));
        menu.deleteMenu(MenuPossibleEnum.DELETE);
        //when
        menuService.deleteMenu(authUser, storeId, menuId);
        //then
        assertNotNull(menu);
        assertEquals(menu.getMenuName(), requestDto.getMenuName());
        assertEquals(menu.getMenuInfo(), requestDto.getMenuInfo());
        assertEquals(menu.getMenuPrice(), requestDto.getMenuPrice());
        assertEquals(menu.getMenuPrice(), requestDto.getMenuPrice());
        assertEquals(menu.getMenuPossibleEnum(), MenuPossibleEnum.DELETE);
        assertEquals(menu.getMenuCategoryEnum(), requestDto.getMenuCategoryEnum());
    }
}
