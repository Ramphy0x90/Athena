package r16a.Athena.exceptions;

public class AuthUserDisabledLockedException extends RuntimeException {
    public AuthUserDisabledLockedException() {
        super("This user is disabled or temporarily locked.");
    }
}
