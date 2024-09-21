package com.sparta.orderking.domain.store.dto;

import com.sparta.orderking.domain.store.entity.StoreStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StoreRequestDto {
    @NotBlank
    private String name;
    private String storeAddress;
    private String storeNumber;
    @Positive(message = "양수만 가능합니다.")
    private int minPrice;
    private LocalTime openTime;
    private LocalTime closeTime;
    private StoreStatus storeStatus;
}
