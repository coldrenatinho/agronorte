package com.agronorte.application.dto;

import com.agronorte.domain.entity.OrderStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class OrderDTO {

    public record CreateOrderRequest(
            @NotNull(message = "Comerciante é obrigatório") UUID merchantId,

            @NotEmpty(message = "Pedido deve ter pelo menos um item") List<OrderItemRequest> items,

            String notes) {
    }

    public record OrderItemRequest(
            @NotNull UUID productId,
            @Min(1) int quantity) {
    }

    public record OrderResponse(
            UUID id,
            UUID buyerId,
            UUID merchantId,
            OrderStatus status,
            List<OrderItemResponse> items,
            BigDecimal totalAmount,
            String notes,
            LocalDateTime createdAt,
            LocalDateTime updatedAt) {
    }

    public record OrderItemResponse(
            UUID id,
            UUID productId,
            String productName,
            BigDecimal unitPrice,
            int quantity,
            BigDecimal subtotal) {
    }
}
