package br.com.ibm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {

    private UUID uuid;
    private String name;
    private LocalDate creationDate;
    private LocalDate updateDate;
    private boolean enable;

    public ProductDto(String name, boolean enable) {
        this.name = name;
        this.enable = enable;
    }
}