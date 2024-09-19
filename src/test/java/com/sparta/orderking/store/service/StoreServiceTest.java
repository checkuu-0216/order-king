package com.sparta.orderking.store.service;

import com.sparta.orderking.config.Auth;
import com.sparta.orderking.config.AuthUser;
import com.sparta.orderking.config.UserRoleEnum;
import com.sparta.orderking.domain.menu.repository.MenuRepository;
import com.sparta.orderking.domain.store.dto.StoreDetailResponseDto;
import com.sparta.orderking.domain.store.dto.StoreResponseDto;
import com.sparta.orderking.domain.store.dto.StoreSimpleRequestDto;
import com.sparta.orderking.domain.store.entity.Store;
import com.sparta.orderking.domain.store.repository.StoreRepository;
import com.sparta.orderking.domain.store.service.StoreService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.sparta.orderking.store.CommonValue.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class StoreServiceTest {

    @Mock
    private StoreRepository storeRepository;
    @Mock
    private MenuRepository menuRepository;
    @InjectMocks
    private StoreService storeService;

    @Test
    void findStoreById(){
        Long storeId=1L;
        given(storeRepository.findById(storeId)).willReturn(Optional.empty());

        NullPointerException exception = assertThrows(NullPointerException.class, ()->{
            storeService.findStoreById(storeId);
        });
        assertEquals("there is no Store",exception.getMessage());
    }

    @Test
    void checkAdmin(){
        RuntimeException exception = assertThrows(RuntimeException.class,()->{
            storeService.checkAdmin(TEST_AUTHUSER2);
        });
        assertEquals("owner only",exception.getMessage());
    }

    @Test
    void saveStore(){
        given(storeRepository.save(any())).willReturn(TEST_STORE);

        StoreResponseDto responseDto = storeService.saveStore(TEST_AUTHUSER,TEST_STOREREQUESTDTO);

        assertNotNull(responseDto);
    }

    @Test
    void updateStore(){
        Long storeId =1L;
        given(storeRepository.findById(storeId)).willReturn(Optional.of(TEST_STORE));

        StoreResponseDto responseDto = storeService.updateStore(TEST_AUTHUSER,storeId,TEST_STOREREQUESTDTO);

        assertNotNull(responseDto);
    }

    @Test
    void getDetailStore_실패테스트_가게찾기실패(){
        Long storeId=1L;
        given(storeRepository.findById(storeId)).willReturn(Optional.empty());

        NullPointerException exception = assertThrows(NullPointerException.class, ()->{
            storeService.findStoreById(storeId);
        });
        assertEquals("there is no Store",exception.getMessage());
    }
    @Test
    void getDetailStore_실패테스트_폐업가계(){
        Long storeId =1L;

        given(storeRepository.findById(storeId)).willReturn(Optional.of(TEST_STORE2));

        RuntimeException exception = assertThrows(RuntimeException.class,()->{
            storeService.getDetailStore(storeId);
        });

        assertEquals("store is closed",exception.getMessage());
    }

    @Test
    void getDetailStore(){
        Long storeId =1L;
        given(storeRepository.findById(storeId)).willReturn(Optional.of(TEST_STORE));

        StoreDetailResponseDto responseDto = storeService.getDetailStore(storeId);

        assertNotNull(responseDto);
    }

    @Test
    void getStore(){
        List<Store> storeList = new ArrayList<>();
        StoreSimpleRequestDto storeSimpleRequestDto = new StoreSimpleRequestDto("name");
        given(storeRepository.findByNameAndService(anyString(),any())).willReturn(storeList);

        List<StoreResponseDto> dtoList = storeService.getStore(storeSimpleRequestDto);

        assertNotNull(dtoList);
    }



}