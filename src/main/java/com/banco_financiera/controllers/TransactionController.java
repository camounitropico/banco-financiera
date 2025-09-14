package com.banco_financiera.controllers;

import com.banco_financiera.dto.TransactionRequestDTO;
import com.banco_financiera.exception.business.BusinessException;
import com.banco_financiera.models.Transaction;
import com.banco_financiera.services.ITransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1/banco-financiera/transactions", produces = {MediaType.APPLICATION_JSON_VALUE})
@RequiredArgsConstructor
@Tag(name = "Transactions", description = "Financial transaction endpoints")
@SecurityRequirement(name = "basicAuth")
public class TransactionController {

    private final ITransactionService transactionService;

    @Operation(summary = "Make a deposit", description = "Deposit money into a financial product account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Deposit completed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Product not found"),
            @ApiResponse(responseCode = "401", description = "Authentication required")
    })
    @PostMapping("/{productId}/deposit")
    public ResponseEntity<Transaction> deposit(
            @Parameter(description = "Product ID to deposit to", required = true)
            @PathVariable Long productId,
            @Parameter(description = "Transaction details", required = true)
            @Valid @RequestBody TransactionRequestDTO transactionRequestDTO) throws BusinessException {

        log.debug("Processing deposit for product ID: {} with amount: {}", productId, transactionRequestDTO.getAmount());
        Transaction transaction = transactionService.deposit(productId, transactionRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(transaction);
    }

    @Operation(summary = "Make a withdrawal", description = "Withdraw money from a financial product account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Withdrawal completed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data or insufficient funds"),
            @ApiResponse(responseCode = "404", description = "Product not found"),
            @ApiResponse(responseCode = "401", description = "Authentication required")
    })
    @PostMapping("/{productId}/withdraw")
    public ResponseEntity<Transaction> withdraw(
            @Parameter(description = "Product ID to withdraw from", required = true)
            @PathVariable Long productId,
            @Parameter(description = "Transaction details", required = true)
            @Valid @RequestBody TransactionRequestDTO transactionRequestDTO) throws BusinessException {

        log.debug("Processing withdrawal for product ID: {} with amount: {}", productId, transactionRequestDTO.getAmount());
        Transaction transaction = transactionService.withdraw(productId, transactionRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(transaction);
    }

    @Operation(summary = "Make a transfer", description = "Transfer money between financial product accounts")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Transfer completed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data, insufficient funds, or transfer to same account"),
            @ApiResponse(responseCode = "404", description = "Source or destination product not found"),
            @ApiResponse(responseCode = "401", description = "Authentication required")
    })
    @PostMapping("/{fromProductId}/transfer/{toProductId}")
    public ResponseEntity<Transaction> transfer(
            @Parameter(description = "Source product ID", required = true)
            @PathVariable Long fromProductId,
            @Parameter(description = "Destination product ID", required = true)
            @PathVariable Long toProductId,
            @Parameter(description = "Transaction details", required = true)
            @Valid @RequestBody TransactionRequestDTO transactionRequestDTO) throws BusinessException {

        log.debug("Processing transfer from product ID: {} to product ID: {} with amount: {}",
                fromProductId, toProductId, transactionRequestDTO.getAmount());
        Transaction transaction = transactionService.transfer(fromProductId, toProductId, transactionRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(transaction);
    }
}