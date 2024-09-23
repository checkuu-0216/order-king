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
    @Pointcut("execution(* com.sparta.orderking.domain.order.controller.OrderController.createOrder(..)) ")
    public void orderCreateMethods() {}

    @Pointcut("execution(* com.sparta.orderking.domain.order.controller.OrderController.updateOrderStatus(..))")
    public void orderUpdateMethods() {}

    @AfterReturning("orderCreateMethods()")
    public void orderCreateLogAfterReturning(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        Long storeId = (Long) args[1]; // 가게 ID

        log.info("주문 요청이 생겼습니다.");
        log.info("요청 시각: " + LocalDateTime.now());
        log.info("가게 ID: " + storeId);
    }

    @AfterReturning("orderUpdateMethods()")
    public void orderUpdateLogAfterReturning(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        Long storeId = (Long) args[1]; // 가게 ID
        Long orderId = (Long) args[2]; // 주문 ID

        log.info("주문 상태 변경 요청이 생겼습니다.");
        log.info("요청 시각: " + LocalDateTime.now());
        log.info("가게 ID: " + storeId);
        log.info("주문 ID: " + orderId);
    }
}
