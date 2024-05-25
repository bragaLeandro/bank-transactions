package br.com.ibm.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class PixKeyDto {

    private UUID id;
    private String type;
    private boolean enable;
    private Long userId;
    private String keyValue;
}
