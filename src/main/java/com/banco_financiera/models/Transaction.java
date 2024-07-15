package com.banco_financiera.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static org.hibernate.type.descriptor.java.JdbcDateJavaType.DATE_FORMAT;

@Data
@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @JsonProperty("transaction_type")
    private String transactionType;

    @Column(nullable = false)
    @JsonProperty("amount")
    private Double amount;

    @Column(nullable = false)
    @JsonProperty("transaction_date")
    @DateTimeFormat(pattern = DATE_FORMAT)
    private LocalDateTime transactionDate;

    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "id", nullable = false)
    private Product product;

    @PrePersist
    protected void onCreate() {
        transactionDate = LocalDateTime.now();
    }
}
