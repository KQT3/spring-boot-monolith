package chaincue.tech.r2dbcbackend2.exeptions;

public class RelationAlreadyExistsException extends RuntimeException {
    public RelationAlreadyExistsException(String message) {
        super(message);
    }
}
