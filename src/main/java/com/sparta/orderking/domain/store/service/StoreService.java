package com.sparta.orderking.domain.store.service;

import com.sparta.orderking.config.AuthUser;
import com.sparta.orderking.domain.menu.entity.MenuPossibleEnum;
import com.sparta.orderking.domain.store.dto.StoreDetailResponseDto;
import com.sparta.orderking.domain.store.dto.StoreRequestDto;
import com.sparta.orderking.domain.store.dto.StoreResponseDto;
import com.sparta.orderking.domain.store.dto.StoreSimpleRequestDto;
import com.sparta.orderking.domain.store.entity.Store;
import com.sparta.orderking.domain.store.entity.StoreAdEnum;
import com.sparta.orderking.domain.store.entity.StoreStatus;
import com.sparta.orderking.domain.store.repository.StoreRepository;
import com.sparta.orderking.domain.menu.entity.Menu;
import com.sparta.orderking.domain.menu.repository.MenuRepository;
import com.sparta.orderking.domain.user.entity.User;
import com.sparta.orderking.domain.user.entity.UserEnum;
import com.sparta.orderking.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StoreService {

    private final StoreRepository storeRepository;
    private final MenuRepository menuRepository;
    private final UserRepository userRepository;

    public Store findStoreById(Long storeId){
        return storeRepository.findById(storeId).orElseThrow(()->new NullPointerException("there is no Store"));
    }

    public Boolean storeIsOpen(Store store){
        return store.getStoreStatus().equals(StoreStatus.OPEN);
    }

    public void checkAdmin(AuthUser authUser){
        if(!authUser.getUserEnum().equals(UserEnum.OWNER)){
            throw new RuntimeException("owner only");
        }
    }
    public User findUser(Long id){
        return userRepository.findById(id).orElseThrow(()->new NullPointerException("no such user"));
    }

    @Transactional
    public StoreResponseDto saveStore(AuthUser authUser, StoreRequestDto storeRequestDto) {
        checkAdmin(authUser);
        User user = findUser(authUser.getId());
        List<Store> storeList = storeRepository.findByUserAndStoreStatus(user,StoreStatus.OPEN);
        if(storeList.size()>=3){
            throw new RuntimeException("already have 3 stores");
        }
        Store store = new Store(storeRequestDto, user);
        Store savedstore = storeRepository.save(store);
        return new StoreResponseDto(savedstore);
    }

    @Transactional
    public StoreResponseDto updateStore(AuthUser authUser, Long storeId, StoreRequestDto storeRequestDto) {
        checkAdmin(authUser);
        User user = findUser(authUser.getId());
        Store store = findStoreById(storeId);

        if(!store.getUser().equals(user)){
            throw new RuntimeException("you are not the owner of the store");
        }
        store.update(storeRequestDto);
        return new StoreResponseDto(store);
    }

    public StoreDetailResponseDto getDetailStore(Long storeId) {
        Store store = findStoreById(storeId);
        if(!storeIsOpen(store)){
            throw new RuntimeException("store is closed");
        }
        return new StoreDetailResponseDto(store,listMenu(storeId,MenuPossibleEnum.DELETE));
    }

    public List<StoreResponseDto> getStore(StoreSimpleRequestDto storeSimpleRequestDto) {
        List<Store> storeList = storeRepository.findByNameAndStoreStatus(
                storeSimpleRequestDto.getName(),
                StoreStatus.OPEN
        );
        List<StoreResponseDto> dtoList = new ArrayList<>();
        for (Store store : storeList) {
            if (store.getStoreAdEnum() == StoreAdEnum.ON) {
                StoreResponseDto dto = new StoreResponseDto(store);
                dtoList.add(dto);
            }
        }
        for (Store store : storeList) {
            if (store.getStoreAdEnum() == StoreAdEnum.OFF) {
                StoreResponseDto dto = new StoreResponseDto(store);
                dtoList.add(dto);
            }
        }
        return dtoList;
    }

    @Transactional
    public void closeStore(AuthUser authUser, Long storeId) {
        checkAdmin(authUser);
        User user = findUser(authUser.getId());
        Store store = storeRepository.findById(storeId).orElseThrow(()->new NullPointerException("no such store"));
        if(!store.getUser().equals(user)){
            throw new RuntimeException("you are not the owner of the store");
        }
        if(store.getStoreStatus().equals(StoreStatus.CLOSED)){
            throw new RuntimeException("already closed");
        }
        store.close();
    }

    public List<Menu> listMenu (Long storeId, MenuPossibleEnum status){
        return menuRepository.findAllByStoreAndMenuPossibleEnumNot(storeId,MenuPossibleEnum.DELETE);
    }

    public void storeAdOn(AuthUser authUser, Long storeId) {
        checkAdmin(authUser);
        User user = findUser(authUser.getId());
        Store store = storeRepository.findById(storeId).orElseThrow(()->new NullPointerException("no such store"));
        if(!store.getUser().equals(user)){
            throw new RuntimeException("you are not the owner of the store");
        }
        if(store.getStoreAdEnum().equals(StoreAdEnum.ON)){
            throw new RuntimeException("already Ad On");
        }
        store.turnOnAd();
    }

    public void storeAdOff(AuthUser authUser, Long storeId) {
        checkAdmin(authUser);
        User user = findUser(authUser.getId());
        Store store = storeRepository.findById(storeId).orElseThrow(()->new NullPointerException("no such store"));
        if(!store.getUser().equals(user)){
            throw new RuntimeException("you are not the owner of the store");
        }
        if(store.getStoreAdEnum().equals(StoreAdEnum.OFF)){
            throw new RuntimeException("already Ad Off");
        }
        store.turnOffAd();
    }
}
