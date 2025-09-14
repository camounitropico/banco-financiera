package com.banco_financiera.services;

import com.banco_financiera.models.Product;
import com.banco_financiera.models.Transaction;
import com.banco_financiera.repositories.ProductRepository;
import com.banco_financiera.repositories.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class TransactionServiceTest {

    @InjectMocks
    TransactionService transactionService;

    @Mock
    TransactionRepository transactionRepository;

    @Mock
    ProductRepository productRepository;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void depositIncreasesAccountBalance() {
        Product product = new Product();
        product.setId(123L);
        product.setAccountBalance(1000.0);
        when(productRepository.findById(123L)).thenReturn(Optional.of(product));
        when(transactionRepository.save(any())).thenReturn(new Transaction());

        Transaction response = transactionService.deposit(123L, 500.0);

        assertEquals(1500.0, product.getAccountBalance());
        assertNotNull(response);
    }

    @Test
    public void withdrawDecreasesAccountBalance() {
        Product product = new Product();
        product.setId(123L);
        product.setAccountBalance(1000.0);
        when(productRepository.findById(123L)).thenReturn(Optional.of(product));
        when(transactionRepository.save(any())).thenReturn(new Transaction());

        Transaction response = transactionService.withdraw(123L, 500.0);

        assertEquals(500.0, product.getAccountBalance());
        assertNotNull(response);
    }

    @Test
    public void withdrawThrowsExceptionWhenInsufficientBalance() {
        Product product = new Product();
        product.setId(123L);
        product.setAccountBalance(1000.0);
        when(productRepository.findById(123L)).thenReturn(Optional.of(product));

        assertThrows(IllegalArgumentException.class, () -> transactionService.withdraw(123L, 1500.0));
    }

    @Test
    public void transferDecreasesFromAccountAndIncreasesToAccount() {
        Product fromProduct = new Product();
        fromProduct.setId(123L);
        fromProduct.setAccountBalance(1000.0);
        when(productRepository.findById(123L)).thenReturn(Optional.of(fromProduct));

        Product toProduct = new Product();
        toProduct.setId(456L);
        toProduct.setAccountBalance(1000.0);
        when(productRepository.findById(456L)).thenReturn(Optional.of(toProduct));
        when(transactionRepository.save(any())).thenReturn(new Transaction());

        Transaction response = transactionService.transfer(123L, 456L, 500.0);

        assertEquals(500.0, fromProduct.getAccountBalance());
        assertEquals(1500.0, toProduct.getAccountBalance());
        assertNotNull(response);
    }

    @Test
    public void transferThrowsExceptionWhenInsufficientBalance() {
        Product fromProduct = new Product();
        fromProduct.setId(123L);
        fromProduct.setAccountBalance(1000.0);
        when(productRepository.findById(123L)).thenReturn(Optional.of(fromProduct));

        Product toProduct = new Product();
        toProduct.setId(456L);
        toProduct.setAccountBalance(1000.0);
        when(productRepository.findById(456L)).thenReturn(Optional.of(toProduct));

        assertThrows(IllegalArgumentException.class, () -> transactionService.transfer(123L, 456L, 1500.0));
    }
}
