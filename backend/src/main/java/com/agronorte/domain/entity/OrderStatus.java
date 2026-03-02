package com.agronorte.domain.entity;

public enum OrderStatus {
    PENDING, // Criado, aguardando confirmação
    CONFIRMED, // Confirmado pelo comerciante
    SHIPPED, // Em rota de entrega
    DELIVERED, // Entregue ao comprador
    CANCELLED // Cancelado
}
