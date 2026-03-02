package com.agronorte.application.port.out;

import com.agronorte.domain.entity.Product;
import com.agronorte.domain.entity.ProductCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface ProductRepository {
    Product save(Product product);

    Optional<Product> findById(UUID id);

    Page<Product> findAllActive(Pageable pageable);

    Page<Product> findByMerchantId(UUID merchantId, Pageable pageable);

    Page<Product> findByCategoryAndActive(ProductCategory category, Pageable pageable);

    void delete(UUID id);
}
