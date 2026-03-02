package com.agronorte.domain.valueobject;

import com.agronorte.domain.exception.DomainException;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Value Object: Price — dinheiro nunca em double, nunca. Aprendi isso do jeito difícil em 2008.
 * BigDecimal com escala fixa. Ponto final.
 */
public record Price(BigDecimal amount) {

    public Price {
        if (amount == null) {
            throw new DomainException("Preço não pode ser nulo.");
        }
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new DomainException("Preço não pode ser negativo.");
        }
        amount = amount.setScale(2, RoundingMode.HALF_UP);
    }

    public static Price of(double value) {
        return new Price(BigDecimal.valueOf(value));
    }

    public static Price of(String value) {
        return new Price(new BigDecimal(value));
    }

    public Price add(Price other) {
        return new Price(this.amount.add(other.amount));
    }

    public Price multiply(int quantity) {
        return new Price(this.amount.multiply(BigDecimal.valueOf(quantity)));
    }

    public boolean isGreaterThan(Price other) {
        return this.amount.compareTo(other.amount) > 0;
    }

    @Override
    public String toString() {
        return "R$ " + amount.toPlainString();
    }
}
