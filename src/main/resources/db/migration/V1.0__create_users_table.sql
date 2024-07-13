CREATE TABLE IF NOT EXISTS users (
        id SERIAL PRIMARY KEY,
        identification_type VARCHAR(50) NOT NULL,
        identification_number BIGSERIAL NOT NULL UNIQUE,
        first_name VARCHAR(100) NOT NULL,
        last_name VARCHAR(100) NOT NULL,
        email VARCHAR(100) NOT NULL UNIQUE,
        birth_date DATE NOT NULL,
        created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
        updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);