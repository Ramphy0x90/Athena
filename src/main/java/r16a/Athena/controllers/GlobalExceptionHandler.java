package r16a.Athena.controllers;

import com.auth0.jwt.exceptions.JWTVerificationException;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import r16a.Athena.exceptions.*;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<?> userAlreadyExists(UserAlreadyExistsException e) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(e.getMessage());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?> entityNotFound(EntityNotFoundException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(e.getMessage());
    }

    @ExceptionHandler(ClientNotFoundException.class)
    public String clientNotFound(ClientNotFoundException e, HttpServletRequest request) {
        request.setAttribute(RequestDispatcher.ERROR_STATUS_CODE, HttpStatus.SERVICE_UNAVAILABLE.value());
        return "forward:/error";
    }

    @ExceptionHandler(ClientAlreadyExistsException.class)
    public ResponseEntity<?> clientAlreadyExists(ClientAlreadyExistsException e) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
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
