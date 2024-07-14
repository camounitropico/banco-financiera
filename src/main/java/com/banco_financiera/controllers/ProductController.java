package com.banco_financiera.controllers;

import com.banco_financiera.core.exceptions.HttpClientException;
import com.banco_financiera.models.Product;
import com.banco_financiera.services.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(value = "/api/v1/products", produces = {MediaType.APPLICATION_JSON_VALUE})
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("all")
    public Iterable<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Optional<Product> product = productService.getProductById(id);
        return product.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/create/{user_id}")
    public Product createProduct(@RequestBody Product product, @PathVariable Long user_id) throws HttpClientException {
        return productService.saveProduct(product, user_id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product product) {
        return productService.getProductById(id)
                .map(ActualProduct -> {
                    ActualProduct.setAccountType(product.getAccountType());
                    ActualProduct.setAccountNumber(product.getAccountNumber());
                    ActualProduct.setStatus(product.getStatus());
                    ActualProduct.setAccountBalance(product.getAccountBalance());
                    ActualProduct.setExentaGMF(product.getExentaGMF());
                    try {
                        productService.saveProduct(ActualProduct, ActualProduct.getUser().getId());
                    } catch (HttpClientException e) {
                        throw new RuntimeException(e);
                    }
                    return ResponseEntity.ok(ActualProduct);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Product> updateProductStatus(@PathVariable Long id, @RequestBody String status) {
        Product product = productService.updateProductState(id, status);
        return ResponseEntity.ok(product);
    }
}
