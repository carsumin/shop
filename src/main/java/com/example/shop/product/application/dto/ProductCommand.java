package com.example.shop.product.application.dto;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * 제품 생성/수정 use-case에 필요한 입력 데이터를 묶는 명령 DTO.
 */
public record ProductCommand(
        UUID sellerId,
        String name,
        String description,
        BigDecimal price,
        Integer stock,
        String status,
        UUID operatorId // 요청을 수정한 운영자/판매자/관리자를 구분하기 위한 값
) {
}
