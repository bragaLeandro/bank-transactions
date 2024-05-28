package br.com.ibm.dto;

import lombok.Data;

@Data
public class TransactionResponseDto {
    public String status;
    public String message;

    public TransactionResponseDto(String status, String message) {
        this.status = status;
        this.message = message;
    }
}
