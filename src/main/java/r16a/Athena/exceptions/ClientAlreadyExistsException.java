package r16a.Athena.exceptions;

public class ClientAlreadyExistsException extends RuntimeException {
    public ClientAlreadyExistsException(String clientName) {
        super("Client '" + clientName + "' already exists");
    }
}
