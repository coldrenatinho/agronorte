package com.agronorte.infrastructure.persistence.repository;

import com.agronorte.infrastructure.persistence.entity.MerchantJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface MerchantJpaRepository extends JpaRepository<MerchantJpaEntity, UUID> {
    Optional<MerchantJpaEntity> findByUserId(UUID userId);

    boolean existsByUserId(UUID userId);
}
