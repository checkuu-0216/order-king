package com.sparta.orderking.domain.store.service;

import com.sparta.orderking.domain.auth.dto.AuthUser;
import com.sparta.orderking.domain.menu.dto.MenuResponseDto;
import com.sparta.orderking.domain.menu.entity.Menu;
import com.sparta.orderking.domain.menu.entity.MenuPossibleEnum;
import com.sparta.orderking.domain.menu.repository.MenuRepository;
import com.sparta.orderking.domain.order.entity.MappingEntity;
import com.sparta.orderking.domain.order.enums.OrderStatus;
import com.sparta.orderking.domain.order.repository.OrderRepository;
import com.sparta.orderking.domain.store.dto.request.StoreNotificationRequestDto;
import com.sparta.orderking.domain.store.dto.request.StoreRequestDto;
import com.sparta.orderking.domain.store.dto.request.StoreSimpleRequestDto;
import com.sparta.orderking.domain.store.dto.response.StoreCheckResponseDto;
import com.sparta.orderking.domain.store.dto.response.StoreDetailResponseDto;
import com.sparta.orderking.domain.store.dto.response.StoreNotificationResponseDto;
import com.sparta.orderking.domain.store.dto.response.StoreResponseDto;
import com.sparta.orderking.domain.store.entity.Store;
import com.sparta.orderking.domain.store.entity.StoreAdEnum;
import com.sparta.orderking.domain.store.entity.StoreStatus;
import com.sparta.orderking.domain.store.repository.StoreRepository;
import com.sparta.orderking.domain.user.entity.User;
import com.sparta.orderking.domain.user.repository.UserRepository;
import com.sparta.orderking.domain.user.service.UserService;
import com.sparta.orderking.exception.UnauthorizedAccessException;
import com.sparta.orderking.exception.WrongConditionException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StoreService {

    private final StoreRepository storeRepository;
    private final MenuRepository menuRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final UserService userService;

    @Transactional
    public StoreResponseDto saveStore(AuthUser authUser, StoreRequestDto storeRequestDto) {
        User user = userService.findUser(authUser.getUserId());
        userService.checkAdmin(user);
        int count = storeRepository.countByUserAndStoreStatus(user, StoreStatus.OPEN);
        if (count >= 3) {
            throw new WrongConditionException("already have 3 stores");
        }
        Store store = new Store(storeRequestDto, user);
        Store savedstore = storeRepository.save(store);
        return new StoreResponseDto(savedstore);
    }

    @Transactional
    public StoreResponseDto updateStore(AuthUser authUser, long storeId, StoreRequestDto storeRequestDto) {
        User user = userService.findUser(authUser.getUserId());
        userService.checkAdmin(user);
        Store store = findStore(storeId);
        checkStoreOwner(store, user);
        store.update(storeRequestDto);
        return new StoreResponseDto(store);
    }

    public StoreDetailResponseDto getDetailStore(long storeId) {
        Store store = storeIsOpen(storeId);
        List<Menu> menuList = menuRepository.findAllByStoreAndMenuPossibleEnum(store, MenuPossibleEnum.SALE);
        List<MenuResponseDto> menudtoList = menuList.stream()
                .map(m -> new MenuResponseDto(
                        m.getMenuName(),
                        m.getMenuInfo(),
                        m.getMenuPrice(),
                        m.getMenuImg(),
                        m.getMenuPossibleEnum(),
                        m.getMenuCategoryEnum()))
                .collect(Collectors.toList());

        return new StoreDetailResponseDto(store, menudtoList);
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
    public void closeStore(AuthUser authUser, long storeId) {
        User user = userService.findUser(authUser.getUserId());
        userService.checkAdmin(user);
        Store store = storeIsOpen(storeId);
        store.close();
    }

    @Transactional
    public void storeAd(AuthUser authUser, long storeId) {
        User user = userService.findUser(authUser.getUserId());
        userService.checkAdmin(user);
        Store store = storeIsOpen(storeId);
        checkStoreOwner(store, user);
        if (store.getStoreAdEnum().equals(StoreAdEnum.ON)) {
            store.turnOffAd();
        }
        else {
            store.turnOnAd();
        }
    }

    public List<StoreCheckResponseDto> checkMyStore(AuthUser authUser, String type, LocalDate startDate, LocalDate endDate) {
        User user = userService.findUser(authUser.getUserId());
        userService.checkAdmin(user);
        List<Store> storeList = storeRepository.findByUserAndStoreStatus(user, StoreStatus.OPEN);

        List<StoreCheckResponseDto> responseList = new ArrayList<>();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("yyyy-MM");
        LocalDate currentDate = LocalDate.now();
        OrderStatus orderStatus = OrderStatus.DELIVERY_COMPLETED;

        for (Store store : storeList) {
            if (startDate == null) {
                startDate = store.getCreatedAt().toLocalDate();
            }
            if (endDate == null) {
                endDate = currentDate;
            }
            if ("daily".equals(type)) {
                // 고객 수와 매출을 하루 단위로 조회
               List<MappingEntity> results = orderRepository.countCustomersAndSales(store.getId(), startDate.atStartOfDay(), endDate.atStartOfDay().plusDays(1), orderStatus);
               addList(results,responseList,store,dateFormatter,startDate);
                }
            else if ("monthly".equalsIgnoreCase(type)) {
                // 월별 고객 수와 매출 조회
                LocalDate monthlyStartDate = startDate.withDayOfMonth(1);
                LocalDate monthlyEndDate = endDate.withDayOfMonth(1);

                List<MappingEntity> results = orderRepository.countMonthlyCustomersAndSales(store.getId(),monthlyStartDate.atStartOfDay(), monthlyEndDate.plusMonths(1).atStartOfDay(), orderStatus);

                addListMonth(results,responseList,store,monthFormatter,startDate);
            }
        }
        return responseList;
    }

    @Transactional
    public StoreNotificationResponseDto changeNotification(AuthUser authUser, long storeId, StoreNotificationRequestDto storeNotificationRequestDto) {
        if (storeNotificationRequestDto.getNotification() == null ||
                storeNotificationRequestDto.getNotification().length() > 255) {
            throw new WrongConditionException("write notification between 1 to 254");
        }
        User user = userService.findUser(authUser.getUserId());
        userService.checkAdmin(user);
        Store store = storeIsOpen(storeId);
        checkStoreOwner(store, user);

        store.updateNotification(storeNotificationRequestDto.getNotification());
        return new StoreNotificationResponseDto(store);
    }

    public Store storeIsOpen(Long storeId) {
        Store store = storeRepository.findById(storeId).orElseThrow(() -> new NullPointerException("no such store"));
        if (store.getStoreStatus().equals(StoreStatus.CLOSED)) {
            throw new WrongConditionException("it is closed store");
        }
        return store;
    }

    public Store findStore(long storeId) {
        return storeRepository.findById(storeId).orElseThrow(() -> new NullPointerException("no such store"));
    }

    public void checkStoreOwner(Store store, User user) {
        if (!store.getUser().equals(user)) {//equals 메서드 재정의 하는것!생각해보기
            throw new UnauthorizedAccessException("you are not the owner of the store");
        }
    }
    private void addList(List<MappingEntity> results,List<StoreCheckResponseDto> responseList,Store store,DateTimeFormatter dateTimeFormatter,LocalDate startDate){
        for (MappingEntity result : results) {
            StoreCheckResponseDto dto = new StoreCheckResponseDto(
                    store.getId(),
                    startDate.format(dateTimeFormatter),
                    result.getCount() != null ? result.getCount() : 0L,
                    result.getSum() != null ? result.getSum() : BigDecimal.ZERO
            );
            responseList.add(dto);
            startDate = startDate.plusDays(1);
        }
    }
    private void addListMonth(List<MappingEntity> results,List<StoreCheckResponseDto> responseList,Store store,DateTimeFormatter dateTimeFormatter,LocalDate startDate){
        for (MappingEntity result : results) {
            StoreCheckResponseDto dto = new StoreCheckResponseDto(
                    store.getId(),
                    startDate.format(dateTimeFormatter),
                    result.getCount() != null ? result.getCount() : 0L,
                    result.getSum() != null ? result.getSum() : BigDecimal.ZERO
            );
            responseList.add(dto);
            startDate = startDate.plusMonths(1);
        }
    }


}
