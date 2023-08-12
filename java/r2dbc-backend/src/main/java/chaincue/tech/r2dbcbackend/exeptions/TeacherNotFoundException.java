package chaincue.tech.r2dbcbackend.exeptions;

public class TeacherNotFoundException extends RuntimeException {
    public TeacherNotFoundException(String id) {
        super(String.format("Teacher not found. Id: %s", id));
    }
}
