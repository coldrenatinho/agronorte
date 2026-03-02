package com.agronorte.domain.exception;

public class ResourceNotFoundException extends DomainException {

    public ResourceNotFoundException(String resource, Object id) {
        super(resource + " não encontrado(a) com id: " + id);
    }

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
