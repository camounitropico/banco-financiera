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


    private Product findProductById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));
    }

    private Transaction buildTransaction(Product product , Double amount, String typeTransaction){
        Transaction transaction = new Transaction();
        transaction.setTransactionType(typeTransaction);
        transaction.setAmount(amount);
        transaction.setProduct(product);

        return transaction;
    }


    public Transaction deposit(Long productId, Double amount) {
        Product product = findProductById(productId);
        product.setAccountBalance(product.getAccountBalance() + amount);
        productRepository.save(product);
        Transaction transaction = buildTransaction( product, amount, TransactionType.DEPOSIT.name());

        return transactionRepository.save(transaction);
    }


    public Transaction withdraw(Long productId, Double amount) {
        Product product = findProductById(productId);
        if (product.getAccountBalance() < amount) {
            throw new IllegalArgumentException("Insufficient balance");
        }
        product.setAccountBalance(product.getAccountBalance() - amount);
        productRepository.save(product);
        Transaction transaction = buildTransaction( product, amount, TransactionType.WITHDRAW.name());
        return transactionRepository.save(transaction);
    }

    public Transaction transfer(Long fromProductId, Long toProductId, Double amount) {
        Product fromProduct = findProductById(fromProductId);
        Product toProduct =findProductById(toProductId);

        if (fromProduct.getAccountBalance() < amount) {
            throw new IllegalArgumentException("Insufficient balance");
        }

        fromProduct.setAccountBalance(fromProduct.getAccountBalance() - amount);
        productRepository.save(fromProduct);

        toProduct.setAccountBalance(toProduct.getAccountBalance() + amount);
        productRepository.save(toProduct);

        Transaction transaction = buildTransaction( fromProduct, amount, TransactionType.TRANSFER.name());

        return transactionRepository.save(transaction);
    }
}
