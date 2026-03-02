-- V4: Tabelas de Pedidos e Itens
CREATE TABLE orders (
    id           BINARY(16)     NOT NULL,
    buyer_id     BINARY(16)     NOT NULL,
    merchant_id  BINARY(16)     NOT NULL,
    status       ENUM('PENDING','CONFIRMED','SHIPPED','DELIVERED','CANCELLED') NOT NULL DEFAULT 'PENDING',
    total_amount DECIMAL(15, 2) NOT NULL DEFAULT 0.00,
    notes        TEXT,
    created_at   DATETIME(6)    NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at   DATETIME(6)    NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    CONSTRAINT pk_orders PRIMARY KEY (id),
    CONSTRAINT fk_orders_buyer FOREIGN KEY (buyer_id) REFERENCES users(id),
    CONSTRAINT fk_orders_merchant FOREIGN KEY (merchant_id) REFERENCES merchants(id)
);

CREATE INDEX idx_orders_buyer ON orders(buyer_id);
CREATE INDEX idx_orders_merchant ON orders(merchant_id);
CREATE INDEX idx_orders_status ON orders(status);
CREATE INDEX idx_orders_merchant_status ON orders(merchant_id, status);
CREATE INDEX idx_orders_created ON orders(created_at DESC);

CREATE TABLE order_items (
    id           BINARY(16)     NOT NULL,
    order_id     BINARY(16)     NOT NULL,
    product_id   BINARY(16)     NOT NULL,
    product_name VARCHAR(200)   NOT NULL,
    unit_price   DECIMAL(15, 2) NOT NULL,
    quantity     INT            NOT NULL,
    subtotal     DECIMAL(15, 2) NOT NULL,
    CONSTRAINT pk_order_items PRIMARY KEY (id),
    CONSTRAINT fk_order_items_order FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
    CONSTRAINT chk_order_items_qty CHECK (quantity > 0),
    CONSTRAINT chk_order_items_price CHECK (unit_price >= 0)
);

CREATE INDEX idx_order_items_order ON order_items(order_id);
