package com.agronorte.domain.entity;

import com.agronorte.domain.exception.DomainException;
import com.agronorte.domain.valueobject.Email;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entidade de domínio pura — ZERO dependência de framework.
 * O dia que você colocar @Entity aqui, é o dia que o Clean Architecture morreu.
 */
@Getter
public class User {

    private final UUID id;
    private String name;
    private Email email;
    private String passwordHash;
    private UserRole role;
    private boolean active;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Factory method: cria novo usuário com invariantes garantidas
    public static User create(String name, String email, String passwordHash, UserRole role) {
        validateName(name);
        return new User(
                UUID.randomUUID(),
                name.trim(),
                new Email(email),
                passwordHash,
                role,
                true,
                LocalDateTime.now(),
                LocalDateTime.now());
    }

    // Reconstituição a partir da persistência (sem lógica de criação)
    public static User reconstitute(UUID id, String name, String email, String passwordHash,
            UserRole role, boolean active,
            LocalDateTime createdAt, LocalDateTime updatedAt) {
        return new User(id, name, new Email(email), passwordHash, role, active, createdAt, updatedAt);
    }

    private User(UUID id, String name, Email email, String passwordHash,
            UserRole role, boolean active, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = role;
        this.active = active;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public void deactivate() {
        this.active = false;
        this.updatedAt = LocalDateTime.now();
    }

    public void changeName(String newName) {
        validateName(newName);
        this.name = newName.trim();
        this.updatedAt = LocalDateTime.now();
    }

    public boolean isAdmin() {
        return UserRole.ADMIN.equals(this.role);
    }

    public boolean isMerchant() {
        return UserRole.MERCHANT.equals(this.role);
    }

    public String getEmailValue() {
        return email.value();
    }

    private static void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new DomainException("Nome do usuário não pode ser vazio.");
        }
        if (name.trim().length() < 3) {
            throw new DomainException("Nome deve ter pelo menos 3 caracteres.");
        }
    }
}
