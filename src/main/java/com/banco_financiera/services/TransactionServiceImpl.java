package com.banco_financiera.services;

import com.banco_financiera.dto.TransactionRequestDTO;
import com.banco_financiera.enums.TransactionType;
import com.banco_financiera.exception.business.BusinessException;
import com.banco_financiera.exception.business.InsufficientFundsException;
import com.banco_financiera.exception.business.TransferToSameAccountException;
import com.banco_financiera.exception.service.TransactionProcessingException;
import com.banco_financiera.models.Product;
import com.banco_financiera.models.Transaction;
import com.banco_financiera.repositories.ProductRepository;
import com.banco_financiera.repositories.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class TransactionServiceImpl implements ITransactionService {

    private final TransactionRepository transactionRepository;
    private final ProductRepository productRepository;
    private final IProductService productService;

    @Override
    public Transaction deposit(Long productId, TransactionRequestDTO transactionRequestDTO) throws BusinessException {
        log.debug("Processing deposit for product ID: {} with amount: {}", productId, transactionRequestDTO.getAmount());

        try {
            Product product = productService.getActiveProductById(productId);
            BigDecimal amount = transactionRequestDTO.getAmount();

            // Update account balance
            BigDecimal newBalance = BigDecimal.valueOf(product.getAccountBalance()).add(amount);
            product.setAccountBalance(newBalance.doubleValue());
            productRepository.save(product);

            // Create transaction record
            Transaction transaction = buildTransaction(product, amount.doubleValue(), TransactionType.DEPOSIT.name());
            Transaction savedTransaction = transactionRepository.save(transaction);

            log.info("Deposit transaction completed successfully for product ID: {} with amount: {}", productId, amount);
            return savedTransaction;

        } catch (Exception e) {
            log.error("Error processing deposit for product {}: {}", productId, e.getMessage());
            throw new TransactionProcessingException("DEPOSIT", productId, "Failed to process deposit transaction", e);
        }
    }

    @Override
    public Transaction withdraw(Long productId, TransactionRequestDTO transactionRequestDTO) throws BusinessException {
        log.debug("Processing withdrawal for product ID: {} with amount: {}", productId, transactionRequestDTO.getAmount());

        try {
            Product product = productService.getActiveProductById(productId);
            BigDecimal amount = transactionRequestDTO.getAmount();
            BigDecimal currentBalance = BigDecimal.valueOf(product.getAccountBalance());

            // Validate sufficient funds
            validateSufficientFunds(currentBalance, amount, productId);

            // Update account balance
            BigDecimal newBalance = currentBalance.subtract(amount);
            product.setAccountBalance(newBalance.doubleValue());
            productRepository.save(product);

            // Create transaction record
            Transaction transaction = buildTransaction(product, amount.doubleValue(), TransactionType.WITHDRAW.name());
            Transaction savedTransaction = transactionRepository.save(transaction);

            log.info("Withdrawal transaction completed successfully for product ID: {} with amount: {}", productId, amount);
            return savedTransaction;

        } catch (BusinessException e) {
            throw e; // Re-throw business exceptions
        } catch (Exception e) {
            log.error("Error processing withdrawal for product {}: {}", productId, e.getMessage());
            throw new TransactionProcessingException("WITHDRAW", productId, "Failed to process withdrawal transaction", e);
        }
    }

    @Override
    public Transaction transfer(Long fromProductId, Long toProductId, TransactionRequestDTO transactionRequestDTO) throws BusinessException {
        log.debug("Processing transfer from product ID: {} to product ID: {} with amount: {}",
                fromProductId, toProductId, transactionRequestDTO.getAmount());

        try {
            // Validate transfer is not to same account
            validateTransferNotSameAccount(fromProductId, toProductId);

            Product fromProduct = productService.getActiveProductById(fromProductId);
            Product toProduct = productService.getActiveProductById(toProductId);

            BigDecimal amount = transactionRequestDTO.getAmount();
            BigDecimal fromCurrentBalance = BigDecimal.valueOf(fromProduct.getAccountBalance());

            // Validate sufficient funds
            validateSufficientFunds(fromCurrentBalance, amount, fromProductId);

            // Update balances
            BigDecimal newFromBalance = fromCurrentBalance.subtract(amount);
            fromProduct.setAccountBalance(newFromBalance.doubleValue());
            productRepository.save(fromProduct);

            BigDecimal toCurrentBalance = BigDecimal.valueOf(toProduct.getAccountBalance());
            BigDecimal newToBalance = toCurrentBalance.add(amount);
            toProduct.setAccountBalance(newToBalance.doubleValue());
            productRepository.save(toProduct);

            // Create transaction record (from source account perspective)
            Transaction transaction = buildTransaction(fromProduct, amount.doubleValue(), TransactionType.TRANSFER.name());
            Transaction savedTransaction = transactionRepository.save(transaction);

            log.info("Transfer transaction completed successfully from product ID: {} to product ID: {} with amount: {}",
                    fromProductId, toProductId, amount);
            return savedTransaction;

        } catch (BusinessException e) {
            throw e; // Re-throw business exceptions
        } catch (Exception e) {
            log.error("Error processing transfer from product {} to product {}: {}", fromProductId, toProductId, e.getMessage());
            throw new TransactionProcessingException("TRANSFER", fromProductId, "Failed to process transfer transaction", e);
        }
    }

    @Override
    public void validateSufficientFunds(BigDecimal currentBalance, BigDecimal amount, Long accountId) throws BusinessException {
        if (currentBalance.compareTo(amount) < 0) {
            throw new InsufficientFundsException(accountId, currentBalance, amount);
        }
    }

    @Override
    public void validateTransferNotSameAccount(Long fromProductId, Long toProductId) throws BusinessException {
        if (fromProductId.equals(toProductId)) {
            throw new TransferToSameAccountException(fromProductId);
        }
    }

    private Transaction buildTransaction(Product product, Double amount, String typeTransaction) {
        Transaction transaction = new Transaction();
        transaction.setTransactionType(typeTransaction);
        transaction.setAmount(amount);
        transaction.setProduct(product);
        return transaction;
    }
}