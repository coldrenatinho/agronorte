package com.agronorte.infrastructure.persistence.adapter;

import com.agronorte.application.port.out.OrderRepository;
import com.agronorte.domain.entity.Order;
import com.agronorte.domain.entity.OrderItem;
import com.agronorte.domain.entity.OrderStatus;
import com.agronorte.domain.valueobject.Price;
import com.agronorte.infrastructure.persistence.entity.OrderItemJpaEntity;
import com.agronorte.infrastructure.persistence.entity.OrderJpaEntity;
import com.agronorte.infrastructure.persistence.repository.OrderJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OrderRepositoryAdapter implements OrderRepository {

    private final OrderJpaRepository jpaRepository;

    @Override
    public Order save(Order order) {
        OrderJpaEntity entity = toEntity(order);
        var savedEntity = jpaRepository.save(entity);
        return toDomain(savedEntity);
    }

    @Override
    public Optional<Order> findById(UUID id) {
        return jpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public Page<Order> findByBuyerId(UUID buyerId, Pageable pageable) {
        return jpaRepository.findByBuyerId(buyerId, pageable).map(this::toDomain);
    }

    @Override
    public Page<Order> findByMerchantId(UUID merchantId, Pageable pageable) {
        return jpaRepository.findByMerchantId(merchantId, pageable).map(this::toDomain);
    }

    @Override
    public Page<Order> findByMerchantIdAndStatus(UUID merchantId, OrderStatus status, Pageable pageable) {
        return jpaRepository.findByMerchantIdAndStatus(merchantId, status, pageable).map(this::toDomain);
    }

    private OrderJpaEntity toEntity(Order order) {
        OrderJpaEntity entity = OrderJpaEntity.builder()
                .id(order.getId())
                .buyerId(order.getBuyerId())
                .merchantId(order.getMerchantId())
                .status(order.getStatus())
                .totalAmount(order.getTotalAmount().amount())
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .build();

        List<OrderItemJpaEntity> itemEntities = order.getItems().stream()
                .map(item -> OrderItemJpaEntity.builder()
                        .id(item.getId())
                        .productId(item.getProductId())
                        .productName(item.getProductName())
                        .quantity(item.getQuantity())
                        .unitPrice(item.getUnitPrice().amount())
                        .subtotal(item.getSubtotal().amount())
                        .order(entity)
                        .build())
                .collect(Collectors.toList());

        entity.addItems(itemEntities);
        return entity;
    }

    private Order toDomain(OrderJpaEntity entity) {
        List<OrderItem> items = entity.getItems().stream()
                .map(item -> OrderItem.reconstitute(
                        item.getId(),
                        entity.getId(),
                        item.getProductId(),
                        item.getProductName(),
                        new Price(item.getUnitPrice()),
                        item.getQuantity()))
                .collect(Collectors.toList());

        return Order.reconstitute(
                entity.getId(),
                entity.getBuyerId(),
                entity.getMerchantId(),
                entity.getStatus(),
                items,
                new Price(entity.getTotalAmount()),
                "", // notes (we didn't map it to DB yet)
                entity.getCreatedAt(),
                entity.getUpdatedAt());
    }
}
