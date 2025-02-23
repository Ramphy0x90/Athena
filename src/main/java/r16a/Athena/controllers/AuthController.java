package r16a.Athena.controllers;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import r16a.Athena.models.dto.*;
import r16a.Athena.services.AuthService;

@Slf4j
@RestController
@RequestMapping("/{clientId}/api/v1/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserRestricted> registerUser(HttpServletRequest request, @RequestBody UserRegister newUser, @PathVariable Integer clientId) {
        log.info("Create user {}", newUser.getEmail());
        UserRegistered userRegistered = authService.register(request, newUser, clientId);
        return ResponseEntity.status(HttpStatus.OK).header("Location", userRegistered.getRedirectUrl()).build();
    }

    @PostMapping("/register-in-new-client")
    public ResponseEntity<UserRestricted> registerUser(@RequestBody UserRegisterConfirm userRegisterConfirm, @PathVariable Integer clientId) {
        log.info("Register user {} in a new client", userRegisterConfirm.getUsernameEmail());
        authService.registerInClient(userRegisterConfirm.getUsernameEmail(), clientId);
        return ResponseEntity.status(HttpStatus.CREATED).header("Location", "/login").build();
    }

    @PostMapping("/authenticate")
    public ResponseEntity<UserAuthenticated> authenticateUser(HttpServletRequest request, @RequestBody UserAuthenticate user, @PathVariable Integer clientId) {
        log.info("Authenticate user {}", user.getUsernameEmail());
        return new ResponseEntity<>(authService.authenticate(request, user, clientId), HttpStatus.OK);
    }

    @PostMapping("/recover-password-link")
    public ResponseEntity<?> sendPasswordRecoveryLink(@RequestBody UserPasswordRecoverLink userPasswordRecoverLink, @PathVariable Integer clientId) {
        log.info("Request password link {}", userPasswordRecoverLink.getUsernameEmail());
        authService.sendPasswordRecoveryLink(userPasswordRecoverLink, clientId);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @PostMapping("/recover-password")
    public ResponseEntity<?> recoverPassword(@RequestBody UserPasswordRecover user, @PathVariable Integer clientId) {
        log.info("Recover password user {}", user.getUsernameEmail());
        return new ResponseEntity<>(authService.recoverPassword(user), HttpStatus.OK);
    }
}
