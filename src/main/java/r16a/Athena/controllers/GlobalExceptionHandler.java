package r16a.Athena.controllers;

import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import r16a.Athena.exceptions.*;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<?> userAlreadyExists(UserAlreadyExistsException e) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(e.getMessage());
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<?> userNotFound(UserNotFoundException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(e.getMessage());
    }

    @ExceptionHandler(AuthInvalidCredentialsException.class)
    public ResponseEntity<?> invalidCredentials(AuthInvalidCredentialsException e) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(e.getMessage());
    }

    @ExceptionHandler(AuthUserDisabledLockedException.class)
    public ResponseEntity<?> userDisabledLocked(AuthUserDisabledLockedException e) {
        return ResponseEntity
                .status(HttpStatus.LOCKED)
                .body(e.getMessage());
    }

    @ExceptionHandler(AuthTokenNotFoundException.class)
    public ResponseEntity<?> tokenNotProvided(AuthTokenNotFoundException e) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(e.getMessage());
    }

    @ExceptionHandler(JWTVerificationException.class)
    public ResponseEntity<?> invalidToken(JWTVerificationException e) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body("Invalid token");
    }
}
