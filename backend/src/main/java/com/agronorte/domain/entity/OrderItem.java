package com.agronorte.domain.entity;

import com.agronorte.domain.exception.DomainException;
import com.agronorte.domain.valueobject.Price;
import lombok.Getter;

import java.util.UUID;

@Getter
public class OrderItem {

    private final UUID id;
    private final UUID orderId;
    private final UUID productId;
    private final String productName;
    private final Price unitPrice;
    private int quantity;

    public static OrderItem create(UUID orderId, UUID productId,
            String productName, Price unitPrice, int quantity) {
        validateQuantity(quantity);
        return new OrderItem(UUID.randomUUID(), orderId, productId, productName, unitPrice, quantity);
    }

    public static OrderItem reconstitute(UUID id, UUID orderId, UUID productId,
            String productName, Price unitPrice, int quantity) {
        return new OrderItem(id, orderId, productId, productName, unitPrice, quantity);
    }

    private OrderItem(UUID id, UUID orderId, UUID productId,
            String productName, Price unitPrice, int quantity) {
        this.id = id;
        this.orderId = orderId;
        this.productId = productId;
        this.productName = productName;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
    }

    public void increaseQuantity(int amount) {
        validateQuantity(amount);
        this.quantity += amount;
    }

    public Price getSubtotal() {
        return unitPrice.multiply(quantity);
    }

    private static void validateQuantity(int qty) {
        if (qty <= 0)
            throw new DomainException("Quantidade deve ser maior que zero.");
    }
}
