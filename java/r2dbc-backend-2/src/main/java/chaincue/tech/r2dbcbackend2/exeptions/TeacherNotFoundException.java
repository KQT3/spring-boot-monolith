package chaincue.tech.r2dbcbackend2.exeptions;

public class TeacherNotFoundException extends RuntimeException {
    public TeacherNotFoundException(String id) {
        super(String.format("Teacher not found. Id: %s", id));
    }
}
