package com.agronorte.application.port.out;

import com.agronorte.domain.entity.User;

import java.util.Optional;
import java.util.UUID;

/**
 * Port (saída) para persistência de User.
 * A camada de domínio/aplicação fala com esta interface.
 * A infraestrutura (JPA) implementa ela. Isso é o princípio da inversão de
 * dependência valendo.
 */
public interface UserRepository {
    User save(User user);

    Optional<User> findById(UUID id);

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
}
