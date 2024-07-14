package com.banco_financiera.repositories;

import com.banco_financiera.models.Product;
import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<Product, Long> {
}
