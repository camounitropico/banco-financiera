package com.banco_financiera.services;

import com.banco_financiera.core.exceptions.HttpClientException;
import com.banco_financiera.dto.ProductRequestDTO;
import com.banco_financiera.enums.AccountStatus;
import com.banco_financiera.exception.business.AccountInactiveException;
import com.banco_financiera.exception.business.AccountNotFoundException;
import com.banco_financiera.exception.business.UserNotFoundException;
import com.banco_financiera.models.Product;
import com.banco_financiera.models.User;
import com.banco_financiera.repositories.ProductRepository;
import com.banco_financiera.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceImplTest {

    @InjectMocks
    ProductServiceImpl productService;

    @Mock
    ProductRepository productRepository;

    @Mock
    UserRepository userRepository;

    @Test
    public void getAllProductsShouldReturnAllProducts() {
        // Arrange
        Product product1 = new Product();
        product1.setId(1L);
        product1.setAccountType("savings");

        Product product2 = new Product();
        product2.setId(2L);
        product2.setAccountType("current");

        when(productRepository.findAll()).thenReturn(Arrays.asList(product1, product2));

        // Act
        List<Product> result = productService.getAllProducts();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("savings", result.get(0).getAccountType());
        assertEquals("current", result.get(1).getAccountType());
    }

    @Test
    public void getProductByIdShouldReturnProductWhenExists() {
        // Arrange
        Product product = new Product();
        product.setId(1L);
        product.setAccountType("savings");

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        // Act
        Optional<Product> result = productService.getProductById(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
        assertEquals("savings", result.get().getAccountType());
    }

    @Test
    public void getProductByIdShouldReturnEmptyWhenNotExists() {
        // Arrange
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        Optional<Product> result = productService.getProductById(1L);

        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    public void createProductShouldReturnSavingsProduct() throws HttpClientException {
        // Arrange
        ProductRequestDTO productRequest = new ProductRequestDTO();
        productRequest.setAccountType("savings");
        productRequest.setAccountBalance(BigDecimal.valueOf(100000));
        productRequest.setExemptGmf(false);

        User user = new User();
        user.setId(1L);
        user.setFirstName("John");

        Product savedProduct = new Product();
        savedProduct.setId(1L);
        savedProduct.setAccountType("savings");
        savedProduct.setAccountBalance(100000.0);
        savedProduct.setStatus(AccountStatus.ACTIVE.name());
        savedProduct.setUser(user);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(productRepository.save(any(Product.class))).thenReturn(savedProduct);

        // Act
        Product result = productService.createProduct(productRequest, 1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("savings", result.getAccountType());
        assertEquals(100000.0, result.getAccountBalance());
        assertEquals(AccountStatus.ACTIVE.name(), result.getStatus());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    public void createProductShouldReturnCurrentProduct() throws HttpClientException {
        // Arrange
        ProductRequestDTO productRequest = new ProductRequestDTO();
        productRequest.setAccountType("current");
        productRequest.setAccountBalance(BigDecimal.valueOf(50000));
        productRequest.setExemptGmf(true);

        User user = new User();
        user.setId(1L);
        user.setFirstName("Jane");

        Product savedProduct = new Product();
        savedProduct.setId(2L);
        savedProduct.setAccountType("current");
        savedProduct.setAccountBalance(50000.0);
        savedProduct.setStatus(AccountStatus.ACTIVE.name());
        savedProduct.setUser(user);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(productRepository.save(any(Product.class))).thenReturn(savedProduct);

        // Act
        Product result = productService.createProduct(productRequest, 1L);

        // Assert
        assertNotNull(result);
        assertEquals(2L, result.getId());
        assertEquals("current", result.getAccountType());
        assertEquals(50000.0, result.getAccountBalance());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    public void createProductShouldThrowExceptionForNegativeBalance() throws HttpClientException {
        // Arrange
        ProductRequestDTO productRequest = new ProductRequestDTO();
        productRequest.setAccountType("savings");
        productRequest.setAccountBalance(BigDecimal.valueOf(-1000));

        User user = new User();
        user.setId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // Act & Assert
        assertThrows(HttpClientException.class, () -> productService.createProduct(productRequest, 1L));
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    public void createProductShouldThrowExceptionWhenUserNotFound() {
        // Arrange
        ProductRequestDTO productRequest = new ProductRequestDTO();
        productRequest.setAccountType("savings");
        productRequest.setAccountBalance(BigDecimal.valueOf(1000));

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(HttpClientException.class, () -> productService.createProduct(productRequest, 1L));
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    public void updateProductShouldReturnUpdatedProduct() throws HttpClientException {
        // Arrange
        Long productId = 1L;
        ProductRequestDTO productRequest = new ProductRequestDTO();
        productRequest.setAccountType("current");
        productRequest.setAccountBalance(BigDecimal.valueOf(75000));
        productRequest.setExemptGmf(true);

        Product existingProduct = new Product();
        existingProduct.setId(productId);
        existingProduct.setAccountType("savings");
        existingProduct.setAccountBalance(50000.0);

        User user = new User();
        user.setId(1L);
        existingProduct.setUser(user);

        when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct));
        when(productRepository.save(any(Product.class))).thenReturn(existingProduct);

        // Act
        Product result = productService.updateProduct(productId, productRequest);

        // Assert
        assertNotNull(result);
        assertEquals(productId, result.getId());
        verify(productRepository, times(1)).save(existingProduct);
    }

    @Test
    public void updateProductShouldThrowExceptionWhenProductNotFound() {
        // Arrange
        Long productId = 1L;
        ProductRequestDTO productRequest = new ProductRequestDTO();
        productRequest.setAccountType("savings");

        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(AccountNotFoundException.class, () -> productService.updateProduct(productId, productRequest));
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    public void deleteProductShouldDeleteWhenZeroBalance() throws HttpClientException {
        // Arrange
        Long productId = 1L;
        Product product = new Product();
        product.setId(productId);
        product.setAccountBalance(0.0);

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        // Act
        productService.deleteProduct(productId);

        // Assert
        verify(productRepository, times(1)).deleteById(productId);
    }

    @Test
    public void deleteProductShouldThrowExceptionWhenNonZeroBalance() {
        // Arrange
        Long productId = 1L;
        Product product = new Product();
        product.setId(productId);
        product.setAccountBalance(1000.0);

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> productService.deleteProduct(productId));
        verify(productRepository, never()).deleteById(any(Long.class));
    }

    @Test
    public void updateProductStatusShouldReturnUpdatedProduct() throws HttpClientException {
        // Arrange
        Long productId = 1L;
        String newStatus = "INACTIVE";

        Product product = new Product();
        product.setId(productId);
        product.setStatus("ACTIVE");

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        // Act
        Product result = productService.updateProductStatus(productId, newStatus);

        // Assert
        assertNotNull(result);
        assertEquals(productId, result.getId());
        verify(productRepository, times(1)).save(product);
    }

    @Test
    public void updateProductStatusShouldThrowExceptionWhenProductNotFound() {
        // Arrange
        Long productId = 1L;
        String newStatus = "INACTIVE";

        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(AccountNotFoundException.class, () -> productService.updateProductStatus(productId, newStatus));
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    public void getActiveProductByIdShouldReturnActiveProduct() throws HttpClientException {
        // Arrange
        Long productId = 1L;
        Product product = new Product();
        product.setId(productId);
        product.setStatus(AccountStatus.ACTIVE.name());

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        // Act
        Product result = productService.getActiveProductById(productId);

        // Assert
        assertNotNull(result);
        assertEquals(productId, result.getId());
        assertEquals(AccountStatus.ACTIVE.name(), result.getStatus());
    }

    @Test
    public void getActiveProductByIdShouldThrowExceptionWhenInactive() {
        // Arrange
        Long productId = 1L;
        Product product = new Product();
        product.setId(productId);
        product.setStatus(AccountStatus.INACTIVE.name());

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        // Act & Assert
        assertThrows(AccountInactiveException.class, () -> productService.getActiveProductById(productId));
    }

    @Test
    public void getActiveProductByIdShouldThrowExceptionWhenProductNotFound() {
        // Arrange
        Long productId = 1L;
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(AccountNotFoundException.class, () -> productService.getActiveProductById(productId));
    }

    @Test
    public void createProductShouldHandleZeroBalance() throws HttpClientException {
        // Arrange
        ProductRequestDTO productRequest = new ProductRequestDTO();
        productRequest.setAccountType("savings");
        productRequest.setAccountBalance(BigDecimal.ZERO);
        productRequest.setExemptGmf(false);

        User user = new User();
        user.setId(1L);

        Product savedProduct = new Product();
        savedProduct.setId(1L);
        savedProduct.setAccountBalance(0.0);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(productRepository.save(any(Product.class))).thenReturn(savedProduct);

        // Act
        Product result = productService.createProduct(productRequest, 1L);

        // Assert
        assertNotNull(result);
        assertEquals(0.0, result.getAccountBalance());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    public void getAllProductsShouldReturnEmptyListWhenNoProducts() {
        // Arrange
        when(productRepository.findAll()).thenReturn(Arrays.asList());

        // Act
        List<Product> result = productService.getAllProducts();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}