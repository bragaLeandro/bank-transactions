package br.com.ibm.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(name = "TAB_TRANSACTION")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "debitor_user_id")
    private User debitor;

    @ManyToOne
    @JoinColumn(name = "creditor_user_id")
    private User creditor;

    private String accountNumber;

    private BigDecimal amount;

    @CreationTimestamp
    private LocalDateTime transactionDate;
    private String status;

    private String type;

    private String description;

    public Transaction(User debitor, User creditor, String type, String status, BigDecimal amount) {
        this.debitor = debitor;
        this.creditor = creditor;
        this.type = type;
        this.status = status;
        this.amount = amount;
    }

    public Transaction(User creditor, String status, BigDecimal amount) {
        this.creditor = creditor;
        this.status = status;
        this.amount = amount;
    }

    public Transaction(User debitor, User creditor, String type, String status, String description, BigDecimal amount) {
        this.debitor = debitor;
        this.creditor = creditor;
        this.type = type;
        this.status = status;
        this.description = description;
        this.amount = amount;
    }
}