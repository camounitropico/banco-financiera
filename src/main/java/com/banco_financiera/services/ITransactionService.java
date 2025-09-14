package com.banco_financiera.services;

import com.banco_financiera.dto.TransactionRequestDTO;
import com.banco_financiera.exception.business.BusinessException;
import com.banco_financiera.models.Transaction;

import java.math.BigDecimal;

public interface ITransactionService {

    /**
     * Perform a deposit transaction
     * @param productId Product ID to deposit to
     * @param transactionRequestDTO Transaction details
     * @return Created transaction
     * @throws BusinessException if product not found or validation fails
     */
    Transaction deposit(Long productId, TransactionRequestDTO transactionRequestDTO) throws BusinessException;

    /**
     * Perform a withdrawal transaction
     * @param productId Product ID to withdraw from
     * @param transactionRequestDTO Transaction details
     * @return Created transaction
     * @throws BusinessException if product not found, insufficient funds, or validation fails
     */
    Transaction withdraw(Long productId, TransactionRequestDTO transactionRequestDTO) throws BusinessException;

    /**
     * Perform a transfer transaction between accounts
     * @param fromProductId Source product ID
     * @param toProductId Destination product ID
     * @param transactionRequestDTO Transaction details
     * @return Created transaction
     * @throws BusinessException if products not found, insufficient funds, or validation fails
     */
    Transaction transfer(Long fromProductId, Long toProductId, TransactionRequestDTO transactionRequestDTO) throws BusinessException;

    /**
     * Validate if account has sufficient funds for a transaction
     * @param currentBalance Current account balance
     * @param amount Amount to validate
     * @param accountId Account ID for error reporting
     * @throws BusinessException if insufficient funds
     */
    void validateSufficientFunds(BigDecimal currentBalance, BigDecimal amount, Long accountId) throws BusinessException;

    /**
     * Validate if transfer is not to the same account
     * @param fromProductId Source product ID
     * @param toProductId Destination product ID
     * @throws BusinessException if trying to transfer to same account
     */
    void validateTransferNotSameAccount(Long fromProductId, Long toProductId) throws BusinessException;
}