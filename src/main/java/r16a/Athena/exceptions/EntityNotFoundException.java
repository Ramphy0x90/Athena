package r16a.Athena.exceptions;

public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(String entityName, Integer entityId) {
        super(entityName + " with id '" + entityId + "' not found");
    }

    public EntityNotFoundException(String entityName, String field, String searchTerm) {
        super(entityName + " with " + field + " '" + searchTerm + "' not found");
    }
}
