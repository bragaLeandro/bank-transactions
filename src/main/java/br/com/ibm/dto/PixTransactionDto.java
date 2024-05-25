package br.com.ibm.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PixTransactionDto {

    private BigDecimal transferValue;
    private PixKeyDto debitor;
    private PixKeyDto creditor;
    private String status;
    private String description;

}
