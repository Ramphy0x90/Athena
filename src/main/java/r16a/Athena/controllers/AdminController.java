package r16a.Athena.controllers;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import r16a.Athena.models.dto.UserAuthenticate;
import r16a.Athena.models.dto.UserAuthenticated;
import r16a.Athena.services.AuthService;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {
    private final AuthService authService;

    @PostMapping("/authenticate")
    public ResponseEntity<UserAuthenticated> authenticate(HttpServletRequest request, @RequestBody UserAuthenticate user) {
        log.info("Authenticate internal user {}", user.getUsernameEmail());
        return new ResponseEntity<>(authService.authenticateInternal(request, user), HttpStatus.OK);
    }
}
