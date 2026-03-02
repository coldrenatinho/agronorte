-- V2: Tabela de Comerciantes
CREATE TABLE merchants (
    id            BINARY(16)   NOT NULL,
    user_id       BINARY(16)   NOT NULL,
    business_name VARCHAR(200) NOT NULL,
    cnpj          VARCHAR(18),
    phone         VARCHAR(20),
    city          VARCHAR(100),
    state         CHAR(2),
    description   TEXT,
    verified      BOOLEAN      NOT NULL DEFAULT FALSE,
    created_at    DATETIME(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at    DATETIME(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    CONSTRAINT pk_merchants PRIMARY KEY (id),
    CONSTRAINT fk_merchants_user FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT uk_merchants_user UNIQUE (user_id)
);

CREATE INDEX idx_merchants_user ON merchants(user_id);
CREATE INDEX idx_merchants_city ON merchants(city);
