-- V3: Tabela de Produtos
CREATE TABLE products (
    id             BINARY(16)     NOT NULL,
    merchant_id    BINARY(16)     NOT NULL,
    name           VARCHAR(200)   NOT NULL,
    description    TEXT,
    price          DECIMAL(15, 2) NOT NULL,
    stock_quantity INT            NOT NULL DEFAULT 0,
    image_url      VARCHAR(500),
    category       ENUM(
        'GRAOS',
        'HORTIFRUTIGRANJEIROS',
        'INSUMOS',
        'MAQUINARIO',
        'SEMENTES',
        'PECUARIA',
        'OUTROS'
    ) NOT NULL,
    active         BOOLEAN        NOT NULL DEFAULT TRUE,
    created_at     DATETIME(6)    NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at     DATETIME(6)    NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    CONSTRAINT pk_products PRIMARY KEY (id),
    CONSTRAINT fk_products_merchant FOREIGN KEY (merchant_id) REFERENCES merchants(id),
    CONSTRAINT chk_products_price CHECK (price >= 0),
    CONSTRAINT chk_products_stock CHECK (stock_quantity >= 0)
);

CREATE INDEX idx_products_merchant ON products(merchant_id);
CREATE INDEX idx_products_category ON products(category);
CREATE INDEX idx_products_active ON products(active);
CREATE INDEX idx_products_merchant_active ON products(merchant_id, active);
