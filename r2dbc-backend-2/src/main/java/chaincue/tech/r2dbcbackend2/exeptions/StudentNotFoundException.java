package chaincue.tech.r2dbcbackend2.exeptions;

public class StudentNotFoundException extends RuntimeException {
    public StudentNotFoundException(String id) {
        super(String.format("Student not found. Id: %s", id));
    }
}
