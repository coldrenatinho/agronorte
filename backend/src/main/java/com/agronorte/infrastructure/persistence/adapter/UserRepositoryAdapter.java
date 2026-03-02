package com.agronorte.infrastructure.persistence.adapter;

import com.agronorte.application.port.out.UserRepository;
import com.agronorte.domain.entity.User;
import com.agronorte.domain.entity.UserRole;
import com.agronorte.infrastructure.persistence.entity.UserJpaEntity;
import com.agronorte.infrastructure.persistence.repository.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

/**
 * Adapter: converte entre domínio ↔ JPA.
 * O domínio nunca viu um UserJpaEntity.
 * O JPA nunca viu um User de domínio.
 * Isso é separação de responsabilidades de verdade.
 */
@Component
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserRepository {

    private final UserJpaRepository jpaRepository;

    @Override
    public User save(User user) {
        var entity = toEntity(user);
        return toDomain(jpaRepository.save(entity));
    }

    @Override
    public Optional<User> findById(UUID id) {
        return jpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return jpaRepository.findByEmail(email).map(this::toDomain);
    }

    @Override
    public boolean existsByEmail(String email) {
        return jpaRepository.existsByEmail(email);
    }

    private UserJpaEntity toEntity(User user) {
        return UserJpaEntity.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmailValue())
                .passwordHash(user.getPasswordHash())
                .role(user.getRole())
                .active(user.isActive())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

    private User toDomain(UserJpaEntity e) {
        return User.reconstitute(
                e.getId(), e.getName(), e.getEmail(), e.getPasswordHash(),
                e.getRole(), e.isActive(), e.getCreatedAt(), e.getUpdatedAt());
    }
}
