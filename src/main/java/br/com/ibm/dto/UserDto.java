package br.com.ibm.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.Calendar;

@Data
public class UserDto {

    private Long id;
    private String name;
    private String email;
    private LocalDate birthDate;
    private String address;
    private boolean enabled;
    private String password;
    private String accountNumber;
}
