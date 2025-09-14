package com.banco_financiera.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
public class TransactionConfig {
    // Configuration for declarative transaction management
    // @Transactional annotations will be processed
    // Rollback will occur for any RuntimeException or Error
    // Can be customized further if needed
}