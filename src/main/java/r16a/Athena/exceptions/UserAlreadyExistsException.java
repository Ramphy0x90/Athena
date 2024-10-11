package r16a.Athena.exceptions;

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(String userEmail) {
        super("User with email '" + userEmail + "' already exists");
    }
}
