package com.sparta.orderking.domain.store.service;

import com.sparta.orderking.config.AuthUser;
import com.sparta.orderking.domain.menu.entity.Menu;
import com.sparta.orderking.domain.menu.entity.MenuPossibleEnum;
import com.sparta.orderking.domain.menu.repository.MenuRepository;
import com.sparta.orderking.domain.order.repository.OrderRepository;
import com.sparta.orderking.domain.store.dto.request.StoreNotificationRequestDto;
import com.sparta.orderking.domain.store.dto.request.StoreRequestDto;
import com.sparta.orderking.domain.store.dto.request.StoreSimpleRequestDto;
import com.sparta.orderking.domain.store.dto.response.*;
import com.sparta.orderking.domain.store.entity.Store;
import com.sparta.orderking.domain.store.entity.StoreAdEnum;
import com.sparta.orderking.domain.store.entity.StoreStatus;
import com.sparta.orderking.domain.store.repository.StoreRepository;
import com.sparta.orderking.domain.user.entity.User;
import com.sparta.orderking.domain.user.entity.UserEnum;
import com.sparta.orderking.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StoreService {

    private final StoreRepository storeRepository;
    private final MenuRepository menuRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    public void storeIsOpen(Store store) {
        if (store.getStoreStatus().equals(StoreStatus.CLOSED)) {
            throw new RuntimeException("it is closed store");
        }
    }

    public void checkAdmin(AuthUser authUser) {
        if (!authUser.getUserEnum().equals(UserEnum.OWNER)) {
            throw new RuntimeException("owner only");
        }
    }

    public Store findStore(long storeId) {
        return storeRepository.findById(storeId).orElseThrow(() -> new NullPointerException("no such store"));
    }

    public void checkStoreOwner(Store store, User user) {
        if (!store.getUser().equals(user)) {
            throw new RuntimeException("you are not the owner of the store");
        }
    }

    public User findUser(long id) {
        return userRepository.findById(id).orElseThrow(() -> new NullPointerException("no such user"));
    }

    public List<Menu> listMenu(long storeId, MenuPossibleEnum status) {
        return menuRepository.findAllByStoreAndMenuPossibleEnumNot(storeId, MenuPossibleEnum.DELETE);
    }

    @Transactional
    public StoreResponseDto saveStore(AuthUser authUser, StoreRequestDto storeRequestDto) {
        checkAdmin(authUser);
        User user = findUser(authUser.getId());
        List<Store> storeList = storeRepository.findByUserAndStoreStatus(user, StoreStatus.OPEN);
        if (storeList.size() >= 3) {
            throw new RuntimeException("already have 3 stores");
        }
        Store store = new Store(storeRequestDto, user);
        Store savedstore = storeRepository.save(store);
        return new StoreResponseDto(savedstore);
    }

    @Transactional
    public StoreResponseDto updateStore(AuthUser authUser, long storeId, StoreRequestDto storeRequestDto) {
        checkAdmin(authUser);
        User user = findUser(authUser.getId());
        Store store = findStore(storeId);
        checkStoreOwner(store, user);
        store.update(storeRequestDto);
        return new StoreResponseDto(store);
    }

    public StoreDetailResponseDto getDetailStore(long storeId) {
        Store store = findStore(storeId);
        storeIsOpen(store);
        return new StoreDetailResponseDto(store, listMenu(storeId, MenuPossibleEnum.DELETE));
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
        checkAdmin(authUser);
        User user = findUser(authUser.getId());
        Store store = findStore(storeId);
        checkStoreOwner(store, user);
        storeIsOpen(store);
        store.close();
    }
    @Transactional
    public void storeAdOn(AuthUser authUser, long storeId) {
        checkAdmin(authUser);
        User user = findUser(authUser.getId());
        Store store = findStore(storeId);
        storeIsOpen(store);
        checkStoreOwner(store, user);
        if (store.getStoreAdEnum().equals(StoreAdEnum.ON)) {
            throw new RuntimeException("already Ad On");
        }
        store.turnOnAd();
    }
    @Transactional
    public void storeAdOff(AuthUser authUser, long storeId) {
        checkAdmin(authUser);
        User user = findUser(authUser.getId());
        Store store = findStore(storeId);
        storeIsOpen(store);
        checkStoreOwner(store, user);
        if (store.getStoreAdEnum().equals(StoreAdEnum.OFF)) {
            throw new RuntimeException("already Ad Off");
        }
        store.turnOffAd();
    }

    public List<StoreCheckDailyResponseDto> checkDailyMyStore(AuthUser authUser) {
        checkAdmin(authUser);
        User user = findUser(authUser.getId());
        List<Store> storeList = storeRepository.findByUserAndStoreStatus(user, StoreStatus.OPEN);

        List<StoreCheckDailyResponseDto> responseList = new ArrayList<>();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime currentTime = LocalDateTime.now();

        for (Store store : storeList) {
            LocalDateTime createTime = store.getCreatedAt();

            // 하루 단위로 고객 수와 매출을 집계
            LocalDateTime startDate = createTime; // 매장 생성 시간
            LocalDateTime endDate = currentTime; // 현재 시간

            while (startDate.toLocalDate().isBefore(endDate.toLocalDate()) || startDate.toLocalDate().isEqual(endDate.toLocalDate())) {
                // 하루 단위로 끝나는 시간 설정
                LocalDateTime nextDay = startDate.plusDays(1);

                // 고객 수와 매출 조회
                Object[] result = orderRepository.countCustomersAndSales(store.getId(), startDate, nextDay);
                Long dailyCustomers = (Long) result[0];
                Long dailySales = (Long) result[1];

                // DTO 생성 및 추가
                StoreCheckDailyResponseDto dto = new StoreCheckDailyResponseDto(
                        store.getId(),
                        startDate.toLocalDate().format(dateFormatter),
                        dailyCustomers,
                        dailySales
                );
                responseList.add(dto);

                // 다음 날로 이동
                startDate = nextDay;
            }
        }

        return responseList;
    }

    public List<StoreCheckMonthlyResponseDto> checkMonthlyMyStore(AuthUser authUser) {
        checkAdmin(authUser);
        User user = findUser(authUser.getId());
        List<Store> storeList = storeRepository.findByUserAndStoreStatus(user, StoreStatus.OPEN);

        List<StoreCheckMonthlyResponseDto> responseList = new ArrayList<>();
        DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("yyyy-MM");
        LocalDateTime currentTime = LocalDateTime.now();

        for (Store store : storeList) {
            LocalDateTime createTime = store.getCreatedAt();
            LocalDateTime startDate = createTime.withDayOfMonth(1); // 매장 생성 월의 첫 날
            LocalDateTime endDate = currentTime.withDayOfMonth(1); // 현재 월의 첫 날

            while (startDate.isBefore(endDate) || startDate.isEqual(endDate)) {
                // 월별 고객 수와 매출 조회
                Object[] result = orderRepository.countMonthlyCustomersAndSales(store.getId(), startDate);
                Long monthlyCustomers = (Long) result[0];
                Long monthlySales = (Long) result[1];

                // DTO 생성 및 추가
                StoreCheckMonthlyResponseDto dto = new StoreCheckMonthlyResponseDto(
                        store.getId(),
                        startDate.format(monthFormatter),
                        monthlyCustomers,
                        monthlySales
                );
                responseList.add(dto);

                // 다음 달로 이동
                startDate = startDate.plusMonths(1);
            }
        }

        return responseList;
    }
    @Transactional
    public StoreNotificationResponseDto changeNotification(AuthUser authUser, long storeId, StoreNotificationRequestDto storeNotificationRequestDto) {
        if (storeNotificationRequestDto.getNotification() == null ||
                storeNotificationRequestDto.getNotification().length() > 255) {
            throw new RuntimeException("write notification between 1 to 254");
        }
        checkAdmin(authUser);
        User user = findUser(authUser.getId());
        Store store = findStore(storeId);
        storeIsOpen(store);
        checkStoreOwner(store, user);

        store.updateNotification(storeNotificationRequestDto.getNotification());
        return new StoreNotificationResponseDto(store);
    }

}
