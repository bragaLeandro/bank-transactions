package br.com.ibm.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

    @Data
    public class DetailedTransactionDto {

        private BigDecimal amount;
        private String status;
        private String type;
        private LocalDateTime transactionDate;
    }
