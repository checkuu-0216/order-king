package com.sparta.orderking.store.service;

import com.sparta.orderking.config.UserRoleEnum;
import com.sparta.orderking.domain.menu.repository.MenuRepository;
import com.sparta.orderking.domain.store.repository.StoreRepository;
import com.sparta.orderking.domain.store.service.StoreService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.sparta.orderking.store.CommonValue.TEST_AUTHUSER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

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
        given(TEST_AUTHUSER.getUserEnum().equals(UserRoleEnum.OWNER)).willReturn(false);
        RuntimeException exception = assertThrows(RuntimeException.class,()->{
            storeService.checkAdmin(TEST_AUTHUSER);
        });
        assertEquals("owner only",exception.getMessage());

    }
}
