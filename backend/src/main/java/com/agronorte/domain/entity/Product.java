package com.agronorte.domain.entity;

import com.agronorte.domain.exception.DomainException;
import com.agronorte.domain.valueobject.Price;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class Product {

    private final UUID id;
    private String name;
    private String description;
    private Price price;
    private int stockQuantity;
    private String imageUrl;
    private ProductCategory category;
    private final UUID merchantId;
    private boolean active;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static Product create(String name, String description, Price price,
                                  int stockQuantity, String imageUrl,
                                  ProductCategory category, UUID merchantId) {
        validate(name, price, stockQuantity);
        return new Product(
                UUID.randomUUID(), name.trim(), description, price,
                stockQuantity, imageUrl, category, merchantId,
                true, LocalDateTime.now(), LocalDateTime.now()
        );
    }

    public static Product reconstitute(UUID id, String name, String description, Price price,
                                        int stockQuantity, String imageUrl, ProductCategory category,
                                        UUID merchantId, boolean active,
                                        LocalDateTime createdAt, LocalDateTime updatedAt) {
        return new Product(id, name, description, price, stockQuantity, imageUrl,
                category, merchantId, active, createdAt, updatedAt);
    }

    private Product(UUID id, String name, String description, Price price,
                    int stockQuantity, String imageUrl, ProductCategory category,
                    UUID merchantId, boolean active,
                    LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.imageUrl = imageUrl;
        this.category = category;
        this.merchantId = merchantId;
        this.active = active;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public void updateDetails(String name, String description, Price price,
                               String imageUrl, ProductCategory category) {
        validate(name, price, this.stockQuantity);
        this.name = name.trim();
        this.description = description;
        this.price = price;
        this.imageUrl = imageUrl;
        this.category = category;
        this.updatedAt = LocalDateTime.now();
    }

    public void decreaseStock(int quantity) {
        if (quantity <= 0) throw new DomainException("Quantidade deve ser positiva.");
        if (this.stockQuantity < quantity) {
            throw new DomainException("Estoque insuficiente para o produto: " + this.name);
        }
        this.stockQuantity -= quantity;
        this.updatedAt = LocalDateTime.now();
    }

    public void increaseStock(int quantity) {
        if (quantity <= 0) throw new DomainException("Quantidade deve ser positiva.");
        this.stockQuantity += quantity;
        this.updatedAt = LocalDateTime.now();
    }

    public void deactivate() {
        this.active = false;
        this.updatedAt = LocalDateTime.now();
    }

    public boolean hasStock(int quantity) {
        return this.stockQuantity >= quantity;
    }

    private static void validate(String name, Price price, int qty) {
        if (name == null || name.isBlank()) throw new DomainException("Nome do produto obrigatório.");
        if (price == null) throw new DomainException("Preço obrigatório.");
        if (qty < 0) throw new DomainException("Estoque não pode ser negativo.");
    }
}
