CREATE TABLE IF NOT EXISTS transactions (
        id SERIAL PRIMARY KEY,
        transaction_type VARCHAR(255) NOT NULL,
        amount DOUBLE PRECISION NOT NULL,
        transaction_date TIMESTAMP NOT NULL,
        product_id INT NOT NULL,
        FOREIGN KEY (product_id) REFERENCES products (id)
    );