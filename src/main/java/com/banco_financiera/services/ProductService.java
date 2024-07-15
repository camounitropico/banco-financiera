package com.banco_financiera.services;

import com.banco_financiera.core.exceptions.HttpClientException;
import com.banco_financiera.enums.AccountStatus;
import com.banco_financiera.enums.AccountTypes;
import com.banco_financiera.models.Product;
import com.banco_financiera.models.User;
import com.banco_financiera.repositories.ProductRepository;
import com.banco_financiera.repositories.UserRepository;
import com.banco_financiera.utils.EnumUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
public class ProductService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public ProductService(UserRepository userRepository, ProductRepository productRepository) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    public Iterable<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    public Product saveProduct(Product product, Long id) throws HttpClientException {

        try {
            Optional<User> user = userRepository.findById(id);
            boolean isTypeInEnum = EnumUtils.isStringInEnum(product.getAccountType(), AccountTypes.class);
            if (user.isPresent()) {

                if (!isTypeInEnum) {
                    throw new IllegalArgumentException("Invalid account type. Must be 'savings' or 'current'");
                }

                if (product.getAccountType().equalsIgnoreCase(AccountTypes.SAVINGS.name()) && product.getAccountBalance() < 0) {
                    throw new IllegalArgumentException("Savings account balance cannot be less than 0");
                }

                if (product.getAccountType().equalsIgnoreCase(AccountTypes.SAVINGS.name())) {
                    product.setStatus(AccountStatus.ACTIVE.name());
                }

                if (product.getAccountType().equalsIgnoreCase(AccountTypes.CURRENT.name())) {
                    product.setAccountNumber("33" + new Random().nextInt(100000000));
                } else {
                    product.setAccountNumber("53" + new Random().nextInt(100000000));
                }

                product.setUser(user.get());

                return productRepository.save(product);
            } else throw new IllegalArgumentException("User not found");

        } catch (Exception e) {
            throw new HttpClientException(HttpStatus.BAD_REQUEST, e);
        }
    }

    public void deleteProduct(Long id) {
        Optional<Product> product = productRepository.findById(id);
        if (product.isPresent() && product.get().getAccountBalance() == 0) {
            productRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("Cannot delete a product with a non-zero balance");
        }
    }

    public Product updateProductState(Long id, String state) {
        Optional<Product> product = productRepository.findById(id);
        if (product.isPresent()) {
            product.get().setStatus(state);
            return productRepository.save(product.get());
        } else {
            throw new IllegalArgumentException("Product not found");
        }
    }
}
