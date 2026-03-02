package com.agronorte.domain.exception;

/**
 * Exceção base de domínio. Sobe pela pilha até o handler de apresentação.
 * NUNCA deixe exceção de domínio virar 500 sem tratamento — isso acontece
 * quando o time não definiu fronteiras claras entre as camadas.
 */
public class DomainException extends RuntimeException {

    public DomainException(String message) {
        super(message);
    }

    public DomainException(String message, Throwable cause) {
        super(message, cause);
    }
}
