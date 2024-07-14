CREATE TABLE IF NOT EXISTS products (
        id SERIAL PRIMARY KEY,
        account_type VARCHAR(255) NOT NULL,
        account_number VARCHAR(255) UNIQUE NOT NULL,
        status VARCHAR(255) NOT NULL,
        account_balance DOUBLE PRECISION NOT NULL,
        exenta_gmf BOOLEAN NOT NULL,
        created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
        updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
        user_id INT NOT NULL,
        FOREIGN KEY (user_id) REFERENCES users (id)
    );