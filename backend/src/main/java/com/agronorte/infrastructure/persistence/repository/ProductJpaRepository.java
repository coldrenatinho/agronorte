package com.agronorte.infrastructure.persistence.repository;

import com.agronorte.domain.entity.ProductCategory;
import com.agronorte.infrastructure.persistence.entity.ProductJpaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProductJpaRepository extends JpaRepository<ProductJpaEntity, UUID> {
    Page<ProductJpaEntity> findAllByActiveTrue(Pageable pageable);

    Page<ProductJpaEntity> findByMerchantId(UUID merchantId, Pageable pageable);

    Page<ProductJpaEntity> findByCategoryAndActiveTrue(ProductCategory category, Pageable pageable);
}
