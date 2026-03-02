package com.agronorte.domain.entity;

import com.agronorte.domain.exception.BusinessRuleException;
import com.agronorte.domain.valueobject.Price;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Agregado Order. A raiz do agregado controla tudo dentro dele.
 * OrderItem nunca deve ser modificado fora do Order — isso é DDD tático de
 * verdade.
 */
@Getter
public class Order {

    private final UUID id;
    private final UUID buyerId;
    private final UUID merchantId;
    private OrderStatus status;
    private final List<OrderItem> items;
    private Price totalAmount;
    private String notes;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static Order create(UUID buyerId, UUID merchantId, String notes) {
        if (buyerId == null)
            throw new BusinessRuleException("Comprador é obrigatório.");
        if (merchantId == null)
            throw new BusinessRuleException("Comerciante é obrigatório.");
        return new Order(
                UUID.randomUUID(), buyerId, merchantId,
                OrderStatus.PENDING, new ArrayList<>(),
                Price.of("0.00"), notes,
                LocalDateTime.now(), LocalDateTime.now());
    }

    public static Order reconstitute(UUID id, UUID buyerId, UUID merchantId, OrderStatus status,
            List<OrderItem> items, Price totalAmount, String notes,
            LocalDateTime createdAt, LocalDateTime updatedAt) {
        return new Order(id, buyerId, merchantId, status, new ArrayList<>(items),
                totalAmount, notes, createdAt, updatedAt);
    }

    private Order(UUID id, UUID buyerId, UUID merchantId, OrderStatus status,
            List<OrderItem> items, Price totalAmount, String notes,
            LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.buyerId = buyerId;
        this.merchantId = merchantId;
        this.status = status;
        this.items = items;
        this.totalAmount = totalAmount;
        this.notes = notes;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public void addItem(Product product, int quantity) {
        if (!OrderStatus.PENDING.equals(this.status)) {
            throw new BusinessRuleException("Só é possível adicionar itens em pedidos pendentes.");
        }
        if (!product.hasStock(quantity)) {
            throw new BusinessRuleException("Estoque insuficiente para: " + product.getName());
        }
        var existingItem = items.stream()
                .filter(i -> i.getProductId().equals(product.getId()))
                .findFirst();

        if (existingItem.isPresent()) {
            existingItem.get().increaseQuantity(quantity);
        } else {
            items.add(OrderItem.create(this.id, product.getId(), product.getName(),
                    product.getPrice(), quantity));
        }
        recalculateTotal();
        this.updatedAt = LocalDateTime.now();
    }

    public void confirm() {
        if (!OrderStatus.PENDING.equals(this.status)) {
            throw new BusinessRuleException("Apenas pedidos PENDENTES podem ser confirmados.");
        }
        if (items.isEmpty()) {
            throw new BusinessRuleException("Pedido não pode ser confirmado sem itens.");
        }
        this.status = OrderStatus.CONFIRMED;
        this.updatedAt = LocalDateTime.now();
    }

    public void ship() {
        if (!OrderStatus.CONFIRMED.equals(this.status)) {
            throw new BusinessRuleException("Apenas pedidos CONFIRMADOS podem ser enviados.");
        }
        this.status = OrderStatus.SHIPPED;
        this.updatedAt = LocalDateTime.now();
    }

    public void deliver() {
        if (!OrderStatus.SHIPPED.equals(this.status)) {
            throw new BusinessRuleException("Apenas pedidos ENVIADOS podem ser entregues.");
        }
        this.status = OrderStatus.DELIVERED;
        this.updatedAt = LocalDateTime.now();
    }

    public void cancel() {
        if (OrderStatus.DELIVERED.equals(this.status)) {
            throw new BusinessRuleException("Pedido já entregue não pode ser cancelado.");
        }
        this.status = OrderStatus.CANCELLED;
        this.updatedAt = LocalDateTime.now();
    }

    public List<OrderItem> getItems() {
        return Collections.unmodifiableList(items);
    }

    private void recalculateTotal() {
        this.totalAmount = items.stream()
                .map(OrderItem::getSubtotal)
                .reduce(Price.of("0.00"), Price::add);
    }
}
