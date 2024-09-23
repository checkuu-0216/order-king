package com.sparta.orderking.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Aspect
@Component
@Slf4j
public class OrderLogging {
    // 주문 생성 및 상태 변경 메서드를 지정하는 포인트컷
    @Pointcut("execution(* com.sparta.orderking.domain.order.controller.OrderController.createOrder(..)) " +
            "|| execution(* com.sparta.orderking.domain.order.controller.OrderController.updateOrderStatus(..))")
    public void orderMethods() {}

    // 주문 생성/상태 변경 이전에 로그 기록
    @Before("orderMethods()")
    public void logBefore(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        Long storeId = (Long) args[0]; // 가게 ID
        Long orderId = (Long) args[1]; // 주문 ID

        // 로그 출력
        log.info("주문 생성/상태 변경 요청이 생겼습니다.");
        log.info("요청 시각: " + LocalDateTime.now());
        log.info("가게 ID: " + storeId);
        log.info("주문 ID: " + orderId);
    }

    // 주문 상태가 변경된 후에 로그 기록
    @AfterReturning("orderMethods()")
    public void logAfter(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        Long storeId = (Long) args[0];
        Long orderId = (Long) args[1];

        // 상태 변경 로그 출력
        log.info("주문 상태가 성공적으로 변경됨");
        log.info("가게 ID: " + storeId);
        log.info("주문 ID: " + orderId);
    }
}
