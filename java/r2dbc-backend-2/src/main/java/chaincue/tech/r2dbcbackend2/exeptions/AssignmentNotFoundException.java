package chaincue.tech.r2dbcbackend2.exeptions;

public class AssignmentNotFoundException extends RuntimeException {
    public AssignmentNotFoundException(String id) {
        super(String.format("Assignment not found. Id: %s", id));
    }
}
