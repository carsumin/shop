package com.example.shop.product.presentation.dto;

import com.example.shop.product.application.dto.ProductCommand;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * 제품 API에서 쓰이는 요청 DTO.
 */
public record ProductRequest(
        String name,
        String description,
        BigDecimal price,
        Integer stock,
        String status,
        String operatorId
) {
    public ProductCommand toCommand() {
        return new ProductCommand(name, description, price, stock, status, parseOperatorId());
    }

    private UUID parseOperatorId() {
        if (operatorId == null || operatorId.isBlank()) {
            return null;
        }
        try {
            return UUID.fromString(operatorId);
        } catch (IllegalArgumentException ignored) {
            return null;
        }
    }
}
