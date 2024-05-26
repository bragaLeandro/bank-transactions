package br.com.ibm.dto;

import lombok.Data;
import java.util.Calendar;

@Data
public class UserDto {

    private Long id;
    private String name;
    private String email;
    private Calendar birthDate;
    private String address;
    private boolean enabled;
    private String password;
    private String accountNumber;
}
