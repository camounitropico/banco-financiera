package com.banco_financiera.services;

import com.banco_financiera.core.exceptions.HttpClientException;
import com.banco_financiera.dto.ProductRequestDTO;
import com.banco_financiera.enums.AccountStatus;
import com.banco_financiera.enums.AccountTypes;
import com.banco_financiera.exception.business.AccountNotFoundException;
import com.banco_financiera.exception.business.UserNotFoundException;
import com.banco_financiera.exception.business.AccountInactiveException;
import com.banco_financiera.models.Product;
import com.banco_financiera.models.User;
import com.banco_financiera.repositories.ProductRepository;
import com.banco_financiera.repositories.UserRepository;
import com.banco_financiera.utils.EnumUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ProductServiceImpl implements IProductService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Product> getAllProducts() {
        log.debug("Getting all products");
        return (List<Product>) productRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Product> getProductById(Long id) {
        log.debug("Getting product by ID: {}", id);
        return productRepository.findById(id);
    }

    @Override
    public Product createProduct(ProductRequestDTO productRequestDTO, Long userId) throws HttpClientException {
        log.debug("Creating product for user ID: {}", userId);

        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new UserNotFoundException(userId));

            // Validate account type
            boolean isTypeInEnum = EnumUtils.isStringInEnum(productRequestDTO.getAccountType(), AccountTypes.class);
            if (!isTypeInEnum) {
                throw new IllegalArgumentException("Invalid account type. Must be 'savings' or 'current'");
            }

            // Validate savings account balance
            if (productRequestDTO.getAccountType().equalsIgnoreCase(AccountTypes.SAVINGS.name())
                && productRequestDTO.getAccountBalance().compareTo(BigDecimal.ZERO) < 0) {
                throw new IllegalArgumentException("Savings account balance cannot be less than 0");
            }

            Product product = new Product();
            product.setAccountType(productRequestDTO.getAccountType());
            product.setAccountBalance(productRequestDTO.getAccountBalance().doubleValue());
            product.setExentaGMF(productRequestDTO.getExemptGmf());
            product.setUser(user);

            // Set default status for savings accounts
            if (productRequestDTO.getAccountType().equalsIgnoreCase(AccountTypes.SAVINGS.name())) {
                product.setStatus(AccountStatus.ACTIVE.name());
            } else {
                product.setStatus(AccountStatus.ACTIVE.name());
            }

            // Generate account number based on account type
            if (productRequestDTO.getAccountType().equalsIgnoreCase(AccountTypes.CURRENT.name())) {
                product.setAccountNumber("33" + new Random().nextInt(100000000));
            } else {
                product.setAccountNumber("53" + new Random().nextInt(100000000));
            }

            Product savedProduct = productRepository.save(product);
            log.info("Product created successfully with ID: {}", savedProduct.getId());
            return savedProduct;

        } catch (Exception e) {
            log.error("Error creating product for user {}: {}", userId, e.getMessage());
            throw new HttpClientException(org.springframework.http.HttpStatus.BAD_REQUEST, e);
        }
    }

    @Override
    public Product updateProduct(Long id, ProductRequestDTO productRequestDTO) throws HttpClientException {
        log.debug("Updating product with ID: {}", id);

        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException(id));

        try {
            // Update allowed fields
            existingProduct.setAccountType(productRequestDTO.getAccountType());
            existingProduct.setAccountBalance(productRequestDTO.getAccountBalance().doubleValue());
            existingProduct.setExentaGMF(productRequestDTO.getExemptGmf());

            Product updatedProduct = productRepository.save(existingProduct);
            log.info("Product updated successfully with ID: {}", updatedProduct.getId());
            return updatedProduct;

        } catch (Exception e) {
            log.error("Error updating product {}: {}", id, e.getMessage());
            throw new HttpClientException(org.springframework.http.HttpStatus.BAD_REQUEST, e);
        }
    }

    @Override
    public void deleteProduct(Long id) throws HttpClientException {
        log.debug("Deleting product with ID: {}", id);

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException(id));

        if (product.getAccountBalance() != 0) {
            throw new IllegalArgumentException("Cannot delete a product with a non-zero balance");
        }

        productRepository.deleteById(id);
        log.info("Product deleted successfully with ID: {}", id);
    }

    @Override
    public Product updateProductStatus(Long id, String status) throws HttpClientException {
        log.debug("Updating product status for ID: {} to: {}", id, status);

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException(id));

        product.setStatus(status);
        Product updatedProduct = productRepository.save(product);

        log.info("Product status updated successfully for ID: {}", id);
        return updatedProduct;
    }

    @Override
    @Transactional(readOnly = true)
    public Product getActiveProductById(Long id) throws HttpClientException {
        log.debug("Getting active product by ID: {}", id);

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException(id));

        if (!AccountStatus.ACTIVE.name().equalsIgnoreCase(product.getStatus())) {
            throw new AccountInactiveException(id, product.getStatus());
        }

        return product;
    }
}