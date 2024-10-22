package com.sparta.orderking.store.service;

import com.sparta.orderking.domain.auth.dto.AuthUser;
import com.sparta.orderking.domain.menu.entity.Menu;
import com.sparta.orderking.domain.menu.repository.MenuRepository;
import com.sparta.orderking.domain.store.dto.request.StoreNotificationRequestDto;
import com.sparta.orderking.domain.store.dto.request.StoreSimpleRequestDto;
import com.sparta.orderking.domain.store.dto.response.StoreDetailResponseDto;
import com.sparta.orderking.domain.store.dto.response.StoreResponseDto;
import com.sparta.orderking.domain.store.entity.Store;
import com.sparta.orderking.domain.store.entity.StoreAdEnum;
import com.sparta.orderking.domain.store.entity.StoreStatus;
import com.sparta.orderking.domain.store.repository.StoreRepository;
import com.sparta.orderking.domain.store.service.StoreService;
import com.sparta.orderking.domain.user.entity.User;
import com.sparta.orderking.domain.user.entity.UserEnum;
import com.sparta.orderking.domain.user.service.UserService;
import com.sparta.orderking.exception.EntityNotFoundException;
import com.sparta.orderking.exception.WrongConditionException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.sparta.orderking.store.CommonValue.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class StoreServiceTest {

    @Mock
    private UserService userService;
    @Mock
    private StoreRepository storeRepository;
    @Mock
    private MenuRepository menuRepository;
    @InjectMocks
    private StoreService storeService;

    @Test
    void storeIsOpen() {
        Store store = TEST_STORE2;
        Long storeId = 1L;
        given(storeRepository.findById(anyLong())).willReturn(Optional.of(store));
        WrongConditionException exception = assertThrows(WrongConditionException.class, () -> {
            storeService.storeIsOpen(storeId);
        });
        assertEquals("it is closed store", exception.getMessage());
    }

    @Test
    void findStore() {
        Long storeId = 1L;
        given(storeRepository.findById(storeId)).willReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            storeService.findStore(storeId);
        });
        assertEquals("no such store", exception.getMessage());
    }


    @Test
    void checkStoreOwner() {
        Store store = TEST_STORE;
        User user = TEST_USER2;
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            storeService.checkStoreOwner(store, user);
        });

        assertEquals("you are not the owner of the store", exception.getMessage());
    }

    @Test
    void saveStore_실패테스트_3개이상() {
        AuthUser authUser = TEST_AUTHUSER;
        User user = new User(UserEnum.OWNER);
        ReflectionTestUtils.setField(user, "id", 1L);
        given(userService.findUser(anyLong())).willReturn(user);
        int count = 3;
        given(storeRepository.countByUserAndStoreStatus(any(), any())).willReturn(count);

        WrongConditionException exception = assertThrows(WrongConditionException.class, () -> {
            storeService.saveStore(authUser, TEST_STOREREQUESTDTO);
        });

        assertEquals("already have 3 stores", exception.getMessage());

    }

    @Test
    void saveStore() {

        given(userService.findUser(anyLong())).willReturn(TEST_USER);
        given(storeRepository.save(any())).willReturn(TEST_STORE);

        StoreResponseDto responseDto = storeService.saveStore(TEST_AUTHUSER, TEST_STOREREQUESTDTO);

        assertNotNull(responseDto);
    }

    @Test
    void updateStore() {
        Long storeId = 1L;

        given(userService.findUser(anyLong())).willReturn(TEST_USER);
        given(storeRepository.findById(storeId)).willReturn(Optional.of(TEST_STORE));

        StoreResponseDto responseDto = storeService.updateStore(TEST_AUTHUSER, storeId, TEST_STOREREQUESTDTO);

        assertNotNull(responseDto);
    }

    @Test
    void getDetailStore_실패테스트_가게찾기실패() {
        Long storeId = 1L;
        given(storeRepository.findById(storeId)).willReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            storeService.findStore(storeId);
        });
        assertEquals("no such store", exception.getMessage());
    }

    @Test
    void getDetailStore_실패테스트_폐업가계() {
        Long storeId = 1L;

        given(storeRepository.findById(storeId)).willReturn(Optional.of(TEST_STORE2));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            storeService.getDetailStore(storeId);
        });

        assertEquals("it is closed store", exception.getMessage());
    }

    @Test
    void getDetailStore() {
        Long storeId = 1L;
        List<Menu> menuList = new ArrayList<>();
        given(storeRepository.findById(storeId)).willReturn(Optional.of(TEST_STORE));
        given(menuRepository.findAllByStoreAndMenuPossibleEnum(any(), any())).willReturn(menuList);
        StoreDetailResponseDto responseDto = storeService.getDetailStore(storeId);

        assertNotNull(responseDto);
    }

    @Test
    void getStore() {
        List<Store> storeList = new ArrayList<>();
        StoreSimpleRequestDto storeSimpleRequestDto = new StoreSimpleRequestDto("name");
        given(storeRepository.findByNameAndStoreStatus(anyString(), any())).willReturn(storeList);

        List<StoreResponseDto> dtoList = storeService.getStore(storeSimpleRequestDto);

        assertNotNull(dtoList);
    }

    @Test
    void closeStore() {
        Long storeId = 1L;
        Store store = mock(Store.class);
        given(storeRepository.findById(anyLong())).willReturn(Optional.of(store));
        given(userService.findUser(anyLong())).willReturn(TEST_USER);
        given(store.getUser()).willReturn(TEST_USER);
        given(store.getStoreStatus()).willReturn(StoreStatus.OPEN);

        storeService.closeStore(TEST_AUTHUSER, storeId);

        verify(store).close();
    }

    @Test
    void storeAd_Off에서_ON() {
        Long storeId = 1L;
        Store store = mock(Store.class);
        User user = TEST_USER;

        given(storeRepository.findById(anyLong())).willReturn(Optional.of(store));
        given(userService.findUser(anyLong())).willReturn(TEST_USER);

        given(store.getUser()).willReturn(user);
        given(store.getStoreStatus()).willReturn(StoreStatus.OPEN);
        given(store.getStoreAdEnum()).willReturn(StoreAdEnum.OFF);

        storeService.storeAd(TEST_AUTHUSER, storeId);

        verify(store).turnOnAd();
    }

    @Test
    void storeAd_ON에서_Off() {
        Long storeId = 1L;
        Store store = mock(Store.class);
        User user = TEST_USER;

        given(storeRepository.findById(anyLong())).willReturn(Optional.of(store));
        given(userService.findUser(anyLong())).willReturn(TEST_USER);

        given(store.getUser()).willReturn(user);
        given(store.getStoreStatus()).willReturn(StoreStatus.OPEN);
        given(store.getStoreAdEnum()).willReturn(StoreAdEnum.ON);

        storeService.storeAd(TEST_AUTHUSER, storeId);

        verify(store).turnOffAd();
    }

    @Test
    void changeNotification_실패글자수() {
        Long storeId = 1L;
        StoreNotificationRequestDto storeNotificationRequestDto = new StoreNotificationRequestDto();

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            storeService.changeNotification(TEST_AUTHUSER, storeId, storeNotificationRequestDto);
        });

        assertEquals("write notification between 1 to 254", exception.getMessage());
    }

    @Test
    void changeNotification() {
        Long storeId = 1L;
        Store store = mock(Store.class);
        User user = TEST_USER;
        StoreNotificationRequestDto storeNotificationRequestDto = new StoreNotificationRequestDto("1234");

        given(storeRepository.findById(anyLong())).willReturn(Optional.of(store));
        given(userService.findUser(anyLong())).willReturn(TEST_USER);

        given(store.getUser()).willReturn(user);
        given(store.getStoreStatus()).willReturn(StoreStatus.OPEN);

        storeService.changeNotification(TEST_AUTHUSER, storeId, storeNotificationRequestDto);

        verify(store).updateNotification(storeNotificationRequestDto.getNotification());
    }

}
