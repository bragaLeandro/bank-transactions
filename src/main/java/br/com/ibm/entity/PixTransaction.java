package br.com.ibm.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(name = "TAB_PIX_TRANSACTION")
public class PixTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "debitor_user_id")
    private User debitor;

    @ManyToOne
    @JoinColumn(name = "creditor_user_id")
    private User creditor;

    @ManyToOne
    @JoinColumn(name = "pix_key_id")
    private PixKey pixKey;

    private BigDecimal value;

    @CreationTimestamp
    private LocalDateTime transactionDate;
    private String status;

    private String description;

    public PixTransaction(User debitor, User creditor, String status, BigDecimal value) {
        this.debitor = debitor;
        this.creditor = creditor;
        this.status = status;
        this.value = value;
    }

    public PixTransaction(User debitor, User creditor, String status, String description, BigDecimal value) {
        this.debitor = debitor;
        this.creditor = creditor;
        this.status = status;
        this.description = description;
        this.value = value;
    }
}