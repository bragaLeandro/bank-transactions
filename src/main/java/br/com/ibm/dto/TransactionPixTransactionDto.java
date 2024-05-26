package br.com.ibm.dto;

import lombok.Data;
import org.springframework.boot.convert.DataSizeUnit;

import java.math.BigDecimal;
import java.time.LocalDateTime;

    @Data
    public class TransactionPixTransactionDto {

        private BigDecimal transferValue;
        private String status;
        private LocalDateTime transferDate;
    }
