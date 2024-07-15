package com.banco_financiera.models;

import com.banco_financiera.enums.AccountStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static org.hibernate.type.descriptor.java.JdbcDateJavaType.DATE_FORMAT;

@Data
@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @JsonProperty("account_type")
    private String accountType;

    @Column(nullable = false, unique = true)
    @JsonProperty("account_number")
    private String accountNumber;

    @Column(nullable = false)
    @JsonProperty("status")
    private String status = AccountStatus.ACTIVE.name();

    @Column(nullable = false)
    @JsonProperty("account_balance")
    private Double accountBalance;

    @Column(name = "exenta_gmf", nullable = false)
    @JsonProperty("exenta_gmf")
    private Boolean exentaGMF;

    @Column(nullable = false, updatable = false)
    @DateTimeFormat(pattern = DATE_FORMAT)
    @CreatedDate
    private LocalDateTime createdAt;

    @Column(nullable = false)
    @DateTimeFormat(pattern = DATE_FORMAT)
    @LastModifiedDate
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

}
