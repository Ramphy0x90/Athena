package r16a.Athena.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import r16a.Athena.models.dto.UserRestricted;
import r16a.Athena.services.UserService;

@Slf4j
@RestController
@RequestMapping("/api/v1/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserRestricted> getUserRestrictedById(@PathVariable Integer id) {
        log.info("Get user restricted with id {}", id);
        return new ResponseEntity<>(userService.getRestrictedById(id), HttpStatus.OK);
    }
}
