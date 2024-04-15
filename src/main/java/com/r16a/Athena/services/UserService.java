package com.r16a.Athena.services;

import com.r16a.Athena.models.User;
import com.r16a.Athena.models.dto.UserRegister;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    public void register(UserRegister newUser) {
        User user = User.builder()
                .name(newUser.getName())
                .surname(newUser.getSurname())
                .email(newUser.getEmail())
                .password(newUser.getPassword())
                .build();
    }
}
