package com.sparta.orderking.domain.order.repository;

import com.sparta.orderking.domain.order.entity.Order;
import com.sparta.orderking.domain.order.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("SELECT o FROM Order o JOIN FETCH o.menuList om JOIN FETCH om.menu WHERE o.id = :orderId")
    Order findByIdWithMenus(Long orderId);
    @Query("SELECT COUNT(DISTINCT o.user.id), SUM(o.price) " +
            "FROM Order o WHERE o.store.id = :storeId AND o.createdAt BETWEEN :startDate AND :endDate " +
            "AND o.orderStatus = :orderStatus")
    Object[] countCustomersAndSales(@Param("storeId") Long storeId,
                                    @Param("startDate") LocalDateTime startDate,
                                    @Param("endDate") LocalDateTime endDate,
                                    @Param("orderStatus") OrderStatus orderStatus);
    @Query("SELECT COUNT(DISTINCT o.user.id), SUM(o.price) " +
            "FROM Order o WHERE o.store.id = :storeId AND " +
            "DATE_FORMAT(o.createdAt, '%Y-%m') = DATE_FORMAT(:date, '%Y-%m') AND " +
            "o.orderStatus = :orderStatus")
    Object[] countMonthlyCustomersAndSales(@Param("storeId") Long storeId,
                                           @Param("date") LocalDateTime date,
                                           @Param("orderStatus") OrderStatus orderStatus);
}