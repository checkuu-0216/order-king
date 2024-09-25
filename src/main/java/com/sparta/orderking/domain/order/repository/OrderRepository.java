package com.sparta.orderking.domain.order.repository;

import com.sparta.orderking.domain.order.entity.MappingEntity;
import com.sparta.orderking.domain.order.entity.Order;
import com.sparta.orderking.domain.order.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("SELECT o FROM Order o JOIN FETCH o.menuList om JOIN FETCH om.menu WHERE o.id = :orderId")
    Order findByIdWithMenus(Long orderId);

    @Query("""
            SELECT COUNT(DISTINCT o.user.id) AS count, SUM(o.price) AS sum
            FROM Order o
            WHERE o.store.id = :storeId AND o.updatedAt BETWEEN :startDate AND :endDate
            AND o.orderStatus = :orderStatus
            GROUP BY FUNCTION('DATE', o.updatedAt)""")
    List<MappingEntity> countCustomersAndSales(@Param("storeId") Long storeId,
                                       @Param("startDate") LocalDateTime startDate,
                                       @Param("endDate") LocalDateTime endDate,
                                       @Param("orderStatus") OrderStatus orderStatus);

    @Query("""
        SELECT COUNT(DISTINCT o.user.id) AS count, SUM(o.price) AS sum
        FROM Order o
        WHERE o.store.id = :storeId AND o.updatedAt BETWEEN :startDate AND :endDate
        AND o.orderStatus = :orderStatus
        GROUP BY EXTRACT(YEAR FROM o.updatedAt), EXTRACT(MONTH FROM o.updatedAt)""")
    List<MappingEntity> countMonthlyCustomersAndSales(@Param("storeId") Long storeId,
                                                      @Param("startDate") LocalDateTime startDate,
                                                      @Param("endDate") LocalDateTime endDate,
                                                      @Param("orderStatus") OrderStatus orderStatus);//group by절 이용해보기
}