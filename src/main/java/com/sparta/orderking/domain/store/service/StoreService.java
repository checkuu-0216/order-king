package com.sparta.orderking.domain.store.service;

import com.sparta.orderking.domain.auth.dto.AuthUser;
import com.sparta.orderking.domain.menu.dto.MenuResponseDto;
import com.sparta.orderking.domain.menu.entity.Menu;
import com.sparta.orderking.domain.menu.entity.MenuPossibleEnum;
import com.sparta.orderking.domain.menu.repository.MenuRepository;
import com.sparta.orderking.domain.order.enums.OrderStatus;
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
import com.sparta.orderking.exception.UnauthorizedAccessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
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
            throw new UnauthorizedAccessException("owner only");
        }
    }

    public Store findStore(long storeId) {
        return storeRepository.findById(storeId).orElseThrow(() -> new NullPointerException("no such store"));
    }

    public void checkStoreOwner(Store store, User user) {
        if (!store.getUser().equals(user)) {
            throw new UnauthorizedAccessException("you are not the owner of the store");
        }
    }

    public User findUser(long id) {
        return userRepository.findById(id).orElseThrow(() -> new NullPointerException("no such user"));
    }

     @Transactional
    public StoreResponseDto saveStore(AuthUser authUser, StoreRequestDto storeRequestDto) {
        checkAdmin(authUser);
        User user = findUser(authUser.getUserId());
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
        User user = findUser(authUser.getUserId());
        Store store = findStore(storeId);
        checkStoreOwner(store, user);
        store.update(storeRequestDto);
        return new StoreResponseDto(store);
    }

    public StoreDetailResponseDto getDetailStore(long storeId) {
        Store store = findStore(storeId);
        storeIsOpen(store);
        List<Menu> menuList = menuRepository.findAllByStoreAndPossibleEnumNot(store,MenuPossibleEnum.DELETE);
        List<MenuResponseDto> menudtoList = new ArrayList<>();
        for(Menu m : menuList){
            MenuResponseDto dto = new MenuResponseDto(m.getMenuName(),
                    m.getMenuInfo(),
                    String.valueOf(m.getMenuPrice()),
                    m.getMenuImg(),
                    m.getPossibleEnum());
            menudtoList.add(dto);
        }
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
        checkAdmin(authUser);
        User user = findUser(authUser.getUserId());
        Store store = findStore(storeId);
        checkStoreOwner(store, user);
        storeIsOpen(store);
        store.close();
    }
    @Transactional
    public void storeAdOn(AuthUser authUser, long storeId) {
        checkAdmin(authUser);
        User user = findUser(authUser.getUserId());
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
        User user = findUser(authUser.getUserId());
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
        User user = findUser(authUser.getUserId());
        List<Store> storeList = storeRepository.findByUserAndStoreStatus(user, StoreStatus.OPEN);

        List<StoreCheckDailyResponseDto> responseList = new ArrayList<>();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate currentDate = LocalDate.now(); // 현재 날짜
        OrderStatus orderStatus = OrderStatus.DELIVERY_COMPLETED; // 주문 상태 설정

        for (Store store : storeList) {
            LocalDateTime createTime = store.getCreatedAt(); // 매장 생성 시간
            LocalDate startDate = createTime.toLocalDate(); // LocalDate로 변환

            // 하루 단위로 고객 수와 매출을 집계
            while (!startDate.isAfter(currentDate)) {
                // 하루 단위로 끝나는 시간 설정
                LocalDateTime startDateTime = startDate.atStartOfDay(); // LocalDate를 LocalDateTime으로 변환
                LocalDateTime nextDayTime = startDate.plusDays(1).atStartOfDay(); // 다음 날의 시작 시간

                // 고객 수와 매출 조회
                Object[] result = orderRepository.countCustomersAndSales(store.getId(), startDateTime, nextDayTime, orderStatus);

                Long dailyCustomers = (result[0] instanceof Number) ? ((Number) result[0]).longValue() : 0L;
                Long dailySales = (result[1] instanceof Number) ? ((Number) result[1]).longValue() : 0L;

                // DTO 생성 및 추가
                StoreCheckDailyResponseDto dto = new StoreCheckDailyResponseDto(
                        store.getId(),
                        startDate.format(dateFormatter),
                        dailyCustomers,
                        dailySales
                );
                responseList.add(dto);

                // 다음 날로 이동
                startDate = startDate.plusDays(1); // LocalDate로 이동
            }
        }
        return responseList;
    }

    public List<StoreCheckMonthlyResponseDto> checkMonthlyMyStore(AuthUser authUser) {
        checkAdmin(authUser);
        User user = findUser(authUser.getUserId());
        List<Store> storeList = storeRepository.findByUserAndStoreStatus(user, StoreStatus.OPEN);

        List<StoreCheckMonthlyResponseDto> responseList = new ArrayList<>();
        DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("yyyy-MM");
        LocalDateTime currentTime = LocalDateTime.now();
        OrderStatus orderStatus = OrderStatus.DELIVERY_COMPLETED;

        for (Store store : storeList) {
            LocalDateTime createTime = store.getCreatedAt();
            LocalDateTime startDate = createTime.withDayOfMonth(1); // 매장 생성 월의 첫 날
            LocalDateTime endDate = currentTime.withDayOfMonth(1); // 현재 월의 첫 날

            while (startDate.isBefore(endDate) || startDate.isEqual(endDate)) {
                // 월별 고객 수와 매출 조회
                Object[] result = orderRepository.countMonthlyCustomersAndSales(store.getId(), startDate, orderStatus);

                Long monthlyCustomers = 0L; // 기본값
                Long monthlySales = 0L; // 기본값

                // 배열의 길이를 확인
                if (result != null && result.length >= 2) {
                    if (result[0] instanceof Number) {
                        monthlyCustomers = ((Number) result[0]).longValue();
                    }
                    if (result[1] instanceof Number) {
                        monthlySales = ((Number) result[1]).longValue();
                    }
                }

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
        User user = findUser(authUser.getUserId());
        Store store = findStore(storeId);
        storeIsOpen(store);
        checkStoreOwner(store, user);

        store.updateNotification(storeNotificationRequestDto.getNotification());
        return new StoreNotificationResponseDto(store);
    }

}
