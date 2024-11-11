package r16a.Athena.exceptions;

public class ClientNotFoundException extends RuntimeException {
    public ClientNotFoundException(Integer clientId) {
        super("Client with id " + clientId + " not found");
    }
}
