package com.r16a.Athena.models;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class User {
    private Integer id;
    private String name;
    private String surname;
    private String email;
    private String password;
    private Date birthday;
}
