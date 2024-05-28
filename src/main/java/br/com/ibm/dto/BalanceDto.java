package br.com.ibm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@AllArgsConstructor
@Data
public class BalanceDto {
    private BigDecimal amount;
}
