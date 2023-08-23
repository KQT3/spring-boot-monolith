package chaincue.tech.r2dbcbackend2.exeptions;

public class RelationNotFoundException extends RuntimeException {
    public RelationNotFoundException(String id) {
        super(String.format("Relation not found. Id: %s", id));
    }
}
