package com.agronorte.application.dto;

import com.agronorte.domain.entity.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class AuthDTO {

    public record RegisterRequest(
            @NotBlank(message = "Nome é obrigatório") String name,

            @NotBlank(message = "E-mail é obrigatório") @Email(message = "E-mail inválido") String email,

            @NotBlank(message = "Senha é obrigatória") @Size(min = 8, message = "Senha deve ter pelo menos 8 caracteres") String password,

            @NotNull(message = "Role é obrigatória") UserRole role) {
    }

    public record LoginRequest(
            @NotBlank(message = "E-mail é obrigatório") String email,

            @NotBlank(message = "Senha é obrigatória") String password) {
    }

    public record AuthResponse(
            String token,
            String tokenType,
            String name,
            String email,
            UserRole role) {
        public static AuthResponse of(String token, String name, String email, UserRole role) {
            return new AuthResponse(token, "Bearer", name, email, role);
        }
    }
}
