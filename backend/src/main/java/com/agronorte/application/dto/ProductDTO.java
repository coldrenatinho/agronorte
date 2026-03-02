package com.agronorte.application.dto;

import com.agronorte.domain.entity.ProductCategory;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.UUID;

public class ProductDTO {

    public record CreateProductRequest(
            @NotBlank(message = "Nome do produto obrigatório") String name,

            String description,

            @NotNull(message = "Preço obrigatório") @DecimalMin(value = "0.01", message = "Preço deve ser positivo") BigDecimal price,

            @Min(value = 0, message = "Estoque não pode ser negativo") int stockQuantity,

            String imageUrl,

            @NotNull(message = "Categoria obrigatória") ProductCategory category) {
    }

    public record UpdateProductRequest(
            @NotBlank String name,
            String description,
            @NotNull @DecimalMin("0.01") BigDecimal price,
            String imageUrl,
            @NotNull ProductCategory category) {
    }

    public record ProductResponse(
            UUID id,
            String name,
            String description,
            BigDecimal price,
            int stockQuantity,
            String imageUrl,
            ProductCategory category,
            UUID merchantId,
            boolean active) {
    }

    public record StockUpdateRequest(
            @Min(1) int quantity,
            StockOperation operation) {
    }

    public enum StockOperation {
        INCREASE, DECREASE
    }
}
