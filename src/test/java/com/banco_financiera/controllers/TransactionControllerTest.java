package com.banco_financiera.controllers;

import com.banco_financiera.models.Transaction;
import com.banco_financiera.services.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class TransactionControllerTest {

    @InjectMocks
    TransactionController transactionController;

    @Mock
    TransactionService transactionService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldCreateDepositTransactionSuccessfully() {
        Transaction transaction = new Transaction();
        transaction.setId(1L);
        when(transactionService.deposit(1L, 100.0)).thenReturn(transaction);

        ResponseEntity<Transaction> response = transactionController.deposit(1L, 100.0);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(1L, response.getBody().getId());
    }

    @Test
    public void shouldCreateWithdrawTransactionSuccessfully() {
        Transaction transaction = new Transaction();
        transaction.setId(1L);
        when(transactionService.withdraw(1L, 100.0)).thenReturn(transaction);

        ResponseEntity<Transaction> response = transactionController.withdraw(1L, 100.0);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(1L, response.getBody().getId());
    }

    @Test
    public void shouldCreateTransferTransactionSuccessfully() {
        Transaction transaction = new Transaction();
        transaction.setId(1L);
        when(transactionService.transfer(1L, 2L, 100.0)).thenReturn(transaction);

        ResponseEntity<Transaction> response = transactionController.transfer(1L, 2L, 100.0);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(1L, response.getBody().getId());
    }
}
