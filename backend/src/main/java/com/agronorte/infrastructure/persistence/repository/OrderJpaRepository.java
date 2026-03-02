package com.agronorte.infrastructure.persistence.repository;

import com.agronorte.domain.entity.OrderStatus;
import com.agronorte.infrastructure.persistence.entity.OrderJpaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OrderJpaRepository extends JpaRepository<OrderJpaEntity, UUID> {

    Page<OrderJpaEntity> findByBuyerId(UUID buyerId, Pageable pageable);

    Page<OrderJpaEntity> findByMerchantId(UUID merchantId, Pageable pageable);

    Page<OrderJpaEntity> findByMerchantIdAndStatus(UUID merchantId, OrderStatus status, Pageable pageable);
}
