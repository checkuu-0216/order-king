package com.sparta.orderking.domain.order.entity;

import lombok.Getter;

import java.math.BigDecimal;

public interface MappingEntity {
    Long getCount();
    BigDecimal getSum();
}
