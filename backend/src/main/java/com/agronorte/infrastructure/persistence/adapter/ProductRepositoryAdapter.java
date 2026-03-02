package com.agronorte.infrastructure.persistence.adapter;

import com.agronorte.application.port.out.ProductRepository;
import com.agronorte.domain.entity.Product;
import com.agronorte.domain.entity.ProductCategory;
import com.agronorte.domain.valueobject.Price;
import com.agronorte.infrastructure.persistence.entity.ProductJpaEntity;
import com.agronorte.infrastructure.persistence.repository.ProductJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ProductRepositoryAdapter implements ProductRepository {

    private final ProductJpaRepository jpaRepository;

    @Override
    public Product save(Product product) {
        return toDomain(jpaRepository.save(toEntity(product)));
    }

    @Override
    public Optional<Product> findById(UUID id) {
        return jpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public Page<Product> findAllActive(Pageable pageable) {
        return jpaRepository.findAllByActiveTrue(pageable).map(this::toDomain);
    }

    @Override
    public Page<Product> findByMerchantId(UUID merchantId, Pageable pageable) {
        return jpaRepository.findByMerchantId(merchantId, pageable).map(this::toDomain);
    }

    @Override
    public Page<Product> findByCategoryAndActive(ProductCategory category, Pageable pageable) {
        return jpaRepository.findByCategoryAndActiveTrue(category, pageable).map(this::toDomain);
    }

    @Override
    public void delete(UUID id) {
        jpaRepository.deleteById(id);
    }

    private ProductJpaEntity toEntity(Product p) {
        return ProductJpaEntity.builder()
                .id(p.getId())
                .name(p.getName())
                .description(p.getDescription())
                .price(p.getPrice().amount())
                .stockQuantity(p.getStockQuantity())
                .imageUrl(p.getImageUrl())
                .category(p.getCategory())
                .merchantId(p.getMerchantId())
                .active(p.isActive())
                .createdAt(p.getCreatedAt())
                .updatedAt(p.getUpdatedAt())
                .build();
    }

    private Product toDomain(ProductJpaEntity e) {
        return Product.reconstitute(
                e.getId(), e.getName(), e.getDescription(), new Price(e.getPrice()),
                e.getStockQuantity(), e.getImageUrl(), e.getCategory(),
                e.getMerchantId(), e.isActive(), e.getCreatedAt(), e.getUpdatedAt());
    }
}
