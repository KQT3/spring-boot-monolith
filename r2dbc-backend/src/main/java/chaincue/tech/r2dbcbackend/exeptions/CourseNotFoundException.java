package chaincue.tech.r2dbcbackend.exeptions;

public class CourseNotFoundException extends RuntimeException {
    public CourseNotFoundException(String id) {
        super(String.format("Course not found. Id: %s", id));
    }
}
