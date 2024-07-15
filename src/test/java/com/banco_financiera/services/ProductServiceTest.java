package com.banco_financiera.services;

import com.banco_financiera.core.exceptions.HttpClientException;
import com.banco_financiera.models.Product;
import com.banco_financiera.models.User;
import com.banco_financiera.repositories.ProductRepository;
import com.banco_financiera.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

public class ProductServiceTest {

    @InjectMocks
    ProductService productService;

    @Mock
    ProductRepository productRepository;

    @Mock
    UserService userService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void shouldReturnProductWhenProductExists() {
        Product product = new Product();
        product.setId(123L);
        when(productRepository.findById(123L)).thenReturn(Optional.of(product));

        Optional<Product> response = productService.getProductById(123L);

        assertTrue(response.isPresent());
        assertEquals(123L, response.get().getId());
    }

    @Test
    public void shouldReturnEmptyWhenProductDoesNotExist() {
        when(productRepository.findById(123L)).thenReturn(Optional.empty());

        Optional<Product> response = productService.getProductById(123L);

        assertFalse(response.isPresent());
    }

    @Test
    public void shouldSaveProductSuccessfully() throws HttpClientException {
        Product product = new Product();
        product.setId(123L);
        User user = new User();
        user.setId(1L);
        when(userService.findById(1L)).thenReturn(Optional.of(user));
        when(productRepository.save(product)).thenReturn(product);

        Product response = productService.saveProduct(product, 1L);

        assertEquals(123L, response.getId());
    }

    @Test
    public void shouldThrowExceptionWhenUserDoesNotExist() {
        Product product = new Product();
        product.setId(123L);
        when(userService.findById(1L)).thenReturn(Optional.empty());

        assertThrows(HttpClientException.class, () -> productService.saveProduct(product, 1L));
    }

    @Test
    public void shouldDeleteProductSuccessfully() {
        Product product = new Product();
        product.setId(123L);
        product.setAccountBalance(0.0);
        when(productRepository.findById(123L)).thenReturn(Optional.of(product));

        productService.deleteProduct(123L);
    }

    @Test
    public void shouldThrowExceptionWhenDeletingProductWithNonZeroBalance() {
        Product product = new Product();
        product.setId(123L);
        product.setAccountBalance(100.0);
        when(productRepository.findById(123L)).thenReturn(Optional.of(product));

        assertThrows(IllegalArgumentException.class, () -> productService.deleteProduct(123L));
    }

    @Test
    public void shouldUpdateProductStateSuccessfully() {
        Product product = new Product();
        product.setId(123L);
        product.setStatus("INACTIVE");
        when(productRepository.findById(123L)).thenReturn(Optional.of(product));
        when(productRepository.save(product)).thenReturn(product);

        Product response = productService.updateProductState(123L, "ACTIVE");

        assertEquals("ACTIVE", response.getStatus());
    }

    @Test
    public void shouldThrowExceptionWhenUpdatingNonExistingProduct() {
        when(productRepository.findById(123L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> productService.updateProductState(123L, "ACTIVE"));
    }
}
