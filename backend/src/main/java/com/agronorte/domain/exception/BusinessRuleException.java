package com.agronorte.domain.exception;

/**
 * Violação de regra de negócio. Diferente de DomainException que é mais genérica,
 * esta indica que o usuário tentou fazer algo que a regra de negócio não permite.
 * Mapeada para HTTP 422 (Unprocessable Entity) — não é 400, não é 500.
 */
public class BusinessRuleException extends DomainException {

    public BusinessRuleException(String message) {
        super(message);
    }
}
