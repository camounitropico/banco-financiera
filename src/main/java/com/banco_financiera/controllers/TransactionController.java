package com.banco_financiera.controllers;

import com.banco_financiera.models.Transaction;
import com.banco_financiera.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/transactions", produces = {MediaType.APPLICATION_JSON_VALUE})
public class TransactionController {

    private final TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/deposit/{productId}")
    public ResponseEntity<Transaction> deposit(@PathVariable Long productId, @RequestBody Double amount) {
        Transaction transaction = transactionService.deposit(productId, amount);
        return new ResponseEntity<>(transaction, HttpStatus.CREATED);
    }

    @PostMapping("/withdraw/{productId}")
    public ResponseEntity<Transaction> withdraw(@PathVariable Long productId, @RequestBody Double amount) {
        Transaction transaction = transactionService.withdraw(productId, amount);
        return new ResponseEntity<>(transaction, HttpStatus.CREATED);
    }

    @PostMapping("/transfer/{fromProductId}/{toProductId}")
    public ResponseEntity<Transaction> transfer(@PathVariable Long fromProductId, @PathVariable Long toProductId, @RequestBody Double amount) {
        Transaction transaction = transactionService.transfer(fromProductId, toProductId, amount);
        return new ResponseEntity<>(transaction, HttpStatus.CREATED);
    }
}
