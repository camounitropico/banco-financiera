package com.banco_financiera.services;

import com.banco_financiera.core.exceptions.HttpClientException;
import com.banco_financiera.dto.ProductRequestDTO;
import com.banco_financiera.models.Product;

import java.util.List;
import java.util.Optional;

public interface IProductService {

    /**
     * Get all products
     * @return List of all products
     */
    List<Product> getAllProducts();

    /**
     * Get product by ID
     * @param id Product ID
     * @return Optional containing the product if found
     */
    Optional<Product> getProductById(Long id);

    /**
     * Create a new product for a user
     * @param productRequestDTO Product data
     * @param userId User ID
     * @return Created product
     * @throws HttpClientException if user not found or validation fails
     */
    Product createProduct(ProductRequestDTO productRequestDTO, Long userId) throws HttpClientException;

    /**
     * Update an existing product
     * @param id Product ID
     * @param productRequestDTO Updated product data
     * @return Updated product
     * @throws HttpClientException if product not found or update fails
     */
    Product updateProduct(Long id, ProductRequestDTO productRequestDTO) throws HttpClientException;

    /**
     * Delete product by ID
     * @param id Product ID
     * @throws HttpClientException if product not found or has non-zero balance
     */
    void deleteProduct(Long id) throws HttpClientException;

    /**
     * Update product status
     * @param id Product ID
     * @param status New status
     * @return Updated product
     * @throws HttpClientException if product not found
     */
    Product updateProductStatus(Long id, String status) throws HttpClientException;

    /**
     * Get active product by ID (for transactions)
     * @param id Product ID
     * @return Product entity
     * @throws HttpClientException if product not found or inactive
     */
    Product getActiveProductById(Long id) throws HttpClientException;
}