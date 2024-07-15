package com.banco_financiera.services;

import com.banco_financiera.enums.TransactionType;
import com.banco_financiera.models.Product;
import com.banco_financiera.models.Transaction;
import com.banco_financiera.repositories.ProductRepository;
import com.banco_financiera.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private ProductRepository productRepository;

    public Transaction deposit(Long productId, Double amount) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));
        product.setAccountBalance(product.getAccountBalance() + amount);
        productRepository.save(product);

        Transaction transaction = new Transaction();
        transaction.setTransactionType(TransactionType.DEPOSIT.name());
        transaction.setAmount(amount);
        transaction.setProduct(product);

        return transactionRepository.save(transaction);
    }

    public Transaction withdraw(Long productId, Double amount) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));
        if (product.getAccountBalance() < amount) {
            throw new IllegalArgumentException("Insufficient balance");
        }
        product.setAccountBalance(product.getAccountBalance() - amount);
        productRepository.save(product);

        Transaction transaction = new Transaction();
        transaction.setTransactionType(TransactionType.WITHDRAW.name());
        transaction.setAmount(amount);
        transaction.setProduct(product);

        return transactionRepository.save(transaction);
    }

    public Transaction transfer(Long fromProductId, Long toProductId, Double amount) {
        Product fromProduct = productRepository.findById(fromProductId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));
        Product toProduct = productRepository.findById(toProductId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        if (fromProduct.getAccountBalance() < amount) {
            throw new IllegalArgumentException("Insufficient balance");
        }

        fromProduct.setAccountBalance(fromProduct.getAccountBalance() - amount);
        productRepository.save(fromProduct);

        toProduct.setAccountBalance(toProduct.getAccountBalance() + amount);
        productRepository.save(toProduct);

        Transaction transaction = new Transaction();
        transaction.setTransactionType(TransactionType.TRANSFER.name());
        transaction.setAmount(amount);
        transaction.setProduct(fromProduct);

        return transactionRepository.save(transaction);
    }
}
