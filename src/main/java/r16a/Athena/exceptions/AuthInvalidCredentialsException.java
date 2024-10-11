package r16a.Athena.exceptions;

public class AuthInvalidCredentialsException extends RuntimeException {
    public AuthInvalidCredentialsException() {
        super("Invalid credentials");
    }
}
