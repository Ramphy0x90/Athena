package r16a.Athena.exceptions;

public class AuthTokenNotFoundException extends RuntimeException {
    public AuthTokenNotFoundException() {
        super("Token was not provided or is invalid");
    }
}
