package com.agronorte.application.port.out;

import com.agronorte.domain.entity.Order;
import com.agronorte.domain.entity.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface OrderRepository {
    Order save(Order order);

    Optional<Order> findById(UUID id);

    Page<Order> findByBuyerId(UUID buyerId, Pageable pageable);

    Page<Order> findByMerchantId(UUID merchantId, Pageable pageable);

    Page<Order> findByMerchantIdAndStatus(UUID merchantId, OrderStatus status, Pageable pageable);
}
