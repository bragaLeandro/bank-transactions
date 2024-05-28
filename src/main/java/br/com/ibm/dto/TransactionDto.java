package br.com.ibm.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TransactionDto {

        private String senderAccountNumber;
        private String destinationAccountNumber;
        private BigDecimal amount;
        private String type;
        private String status;
        private LocalDateTime transactionDate;
}
