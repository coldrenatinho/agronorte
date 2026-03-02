package com.agronorte.domain.entity;

import com.agronorte.domain.exception.DomainException;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class Merchant {

    private final UUID id;
    private final UUID userId;
    private String businessName;
    private String cnpj;
    private String phone;
    private String city;
    private String state;
    private String description;
    private boolean verified;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static Merchant create(UUID userId, String businessName, String cnpj,
            String phone, String city, String state, String description) {
        validateBusinessName(businessName);
        return new Merchant(
                UUID.randomUUID(), userId, businessName, cnpj,
                phone, city, state, description, false,
                LocalDateTime.now(), LocalDateTime.now());
    }

    public static Merchant reconstitute(UUID id, UUID userId, String businessName, String cnpj,
            String phone, String city, String state, String description,
            boolean verified, LocalDateTime createdAt, LocalDateTime updatedAt) {
        return new Merchant(id, userId, businessName, cnpj, phone, city, state,
                description, verified, createdAt, updatedAt);
    }

    private Merchant(UUID id, UUID userId, String businessName, String cnpj,
            String phone, String city, String state, String description,
            boolean verified, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.userId = userId;
        this.businessName = businessName;
        this.cnpj = cnpj;
        this.phone = phone;
        this.city = city;
        this.state = state;
        this.description = description;
        this.verified = verified;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public void verify() {
        this.verified = true;
        this.updatedAt = LocalDateTime.now();
    }

    public void updateProfile(String businessName, String phone,
            String city, String state, String description) {
        validateBusinessName(businessName);
        this.businessName = businessName;
        this.phone = phone;
        this.city = city;
        this.state = state;
        this.description = description;
        this.updatedAt = LocalDateTime.now();
    }

    private static void validateBusinessName(String name) {
        if (name == null || name.isBlank()) {
            throw new DomainException("Razão social obrigatória.");
        }
    }
}
