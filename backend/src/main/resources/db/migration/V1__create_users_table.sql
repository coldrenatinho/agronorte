-- V1: Tabela de Usuários
CREATE TABLE users (
    id         BINARY(16)   NOT NULL,
    name       VARCHAR(150) NOT NULL,
    email      VARCHAR(255) NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    role       ENUM('ADMIN','MERCHANT','BUYER') NOT NULL DEFAULT 'BUYER',
    active     BOOLEAN      NOT NULL DEFAULT TRUE,
    created_at DATETIME(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at DATETIME(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    CONSTRAINT pk_users PRIMARY KEY (id),
    CONSTRAINT uk_users_email UNIQUE (email)
);

CREATE INDEX idx_users_email ON users(email);

INSERT INTO users (id, name, email, password_hash, role, active, created_at, updated_at)
VALUES (
    UNHEX(REPLACE(UUID(), '-', '')),
    'Admin AgroNorte',
    'admin@agronorte.com.br',
    '$2a$12$cWEZffK1TeWMefr3tcY5zOsmxIyrOuPZx36AvVWvnyYtaz4CWN8oe', -- senha: Admin@2024
    'ADMIN',
    TRUE,
    NOW(6),
    NOW(6)
);
