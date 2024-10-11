package r16a.Athena.exceptions;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Integer id) {
        super("User with id '" + id + "' not found");
    }

    public UserNotFoundException(String email) {
        super("User with email '" + email + "' not found");
    }
}
