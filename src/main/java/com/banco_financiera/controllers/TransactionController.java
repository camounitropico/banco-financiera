package com.banco_financiera.controllers;

import com.banco_financiera.models.Transaction;
import com.banco_financiera.requests.TransactionRequest;
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

    @PostMapping("/deposit/{product_id}")
    public ResponseEntity<Transaction> deposit(@PathVariable("product_id") Long productId, @RequestBody TransactionRequest transactionRequest) {
        Transaction transaction = transactionService.deposit(productId, transactionRequest.getAmount());
        return new ResponseEntity<>(transaction, HttpStatus.CREATED);
    }

    @PostMapping("/withdraw/{product_id}")
    public ResponseEntity<Transaction> withdraw(@PathVariable("product_id") Long productId, @RequestBody TransactionRequest transactionRequest) {
        Transaction transaction = transactionService.withdraw(productId, transactionRequest.getAmount());
        return new ResponseEntity<>(transaction, HttpStatus.CREATED);
    }

    @PostMapping("/transfer/{from_product_id}/{to_product_id}")
    public ResponseEntity<Transaction> transfer(@PathVariable("from_product_id") Long fromProductId, @PathVariable("to_product_id") Long toProductId, @RequestBody TransactionRequest transactionRequest) {
        Transaction transaction = transactionService.transfer(fromProductId, toProductId, transactionRequest.getAmount());
        return new ResponseEntity<>(transaction, HttpStatus.CREATED);
    }
}
