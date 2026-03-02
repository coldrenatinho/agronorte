package com.agronorte.application.port.out;

import com.agronorte.domain.entity.Merchant;

import java.util.Optional;
import java.util.UUID;

public interface MerchantRepository {
    Merchant save(Merchant merchant);

    Optional<Merchant> findById(UUID id);

    Optional<Merchant> findByUserId(UUID userId);

    boolean existsByUserId(UUID userId);
}
