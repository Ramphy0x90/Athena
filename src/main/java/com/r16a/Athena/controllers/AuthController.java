package com.r16a.Athena.controllers;

import com.r16a.Athena.models.dto.UserRegister;
import com.r16a.Athena.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("api/v1/auth")
public record AuthController(UserService userService) {

    @PostMapping("register/")
    public void register(@RequestBody UserRegister newUser) {
        log.info("[*] AuthController: register user {}", newUser);
        userService.register(newUser);
    }
}
