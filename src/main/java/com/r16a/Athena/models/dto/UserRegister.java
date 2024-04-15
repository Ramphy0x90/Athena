package com.r16a.Athena.models.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserRegister {
    private String name;
    private String surname;
    private String email;
    private String password;
}
