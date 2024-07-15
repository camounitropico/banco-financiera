package com.banco_financiera.controllers;

import com.banco_financiera.core.exceptions.HttpClientException;
import com.banco_financiera.models.Product;
import com.banco_financiera.services.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class ProductControllerTest {

    @InjectMocks
    ProductController productController;

    @Mock
    ProductService productService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldReturnProductWhenProductExists() {
        Product product = new Product();
        product.setId(123L);
        when(productService.getProductById(123L)).thenReturn(Optional.of(product));

        ResponseEntity<Product> response = productController.getProductById(123L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(123L, response.getBody().getId());
    }

    @Test
    public void shouldReturnNotFoundWhenProductDoesNotExist() {
        when(productService.getProductById(123L)).thenReturn(Optional.empty());

        ResponseEntity<Product> response = productController.getProductById(123L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void shouldCreateProductSuccessfully() throws HttpClientException {
        Product product = new Product();
        product.setId(123L);
        when(productService.saveProduct(product, 1L)).thenReturn(product);

        Product response = productController.createProduct(product, 1L);

        assertEquals(123L, response.getId());
    }

    @Test
    public void shouldUpdateProductSuccessfully() throws HttpClientException {
        Product product = new Product();
        product.setId(123L);
        when(productService.getProductById(123L)).thenReturn(Optional.of(product));
        when(productService.saveProduct(product, product.getUser().getId())).thenReturn(product);

        ResponseEntity<Product> response = productController.updateProduct(123L, product);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(123L, response.getBody().getId());
    }

    @Test
    public void shouldReturnNotFoundWhenUpdatingNonExistingProduct() throws HttpClientException {
        Product product = new Product();
        product.setId(123L);
        when(productService.getProductById(123L)).thenReturn(Optional.empty());

        ResponseEntity<Product> response = productController.updateProduct(123L, product);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void shouldDeleteProductSuccessfully() {
        Product product = new Product();
        product.setId(123L);
        when(productService.getProductById(123L)).thenReturn(Optional.of(product));

        ResponseEntity<Void> response = productController.deleteProduct(123L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(productService, times(1)).deleteProduct(product.getId());
    }

    @Test
    public void shouldReturnNotFoundWhenDeletingNonExistingProduct() {
        when(productService.getProductById(123L)).thenReturn(Optional.empty());

        ResponseEntity<Void> response = productController.deleteProduct(123L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void shouldUpdateProductStatusSuccessfully() {
        Product product = new Product();
        product.setId(123L);
        product.setStatus("ACTIVE");
        when(productService.updateProductState(123L, "ACTIVE")).thenReturn(product);

        ResponseEntity<Product> response = productController.updateProductStatus(123L, "ACTIVE");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("ACTIVE", response.getBody().getStatus());
    }
}
