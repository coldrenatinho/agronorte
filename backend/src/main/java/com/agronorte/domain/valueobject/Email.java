package com.agronorte.domain.valueobject;

import com.agronorte.domain.exception.DomainException;

import java.util.regex.Pattern;

/**
 * Value Object: Email — imutável, autoreferenciado, validação embutida.
 * Você nunca mais vai passar String de email por toda a app e só descobrir que era inválido no banco.
 */
public record Email(String value) {

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[a-zA-Z0-9._%+\\-]+@[a-zA-Z0-9.\\-]+\\.[a-zA-Z]{2,}$");

    public Email {
        if (value == null || value.isBlank()) {
            throw new DomainException("E-mail não pode ser vazio.");
        }
        if (!EMAIL_PATTERN.matcher(value.trim()).matches()) {
            throw new DomainException("E-mail inválido: " + value);
        }
        value = value.trim().toLowerCase();
    }
}
