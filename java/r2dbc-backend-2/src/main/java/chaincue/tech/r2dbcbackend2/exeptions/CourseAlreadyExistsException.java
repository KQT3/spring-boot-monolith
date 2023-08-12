package chaincue.tech.r2dbcbackend2.exeptions;

public class CourseAlreadyExistsException extends RuntimeException {
    public CourseAlreadyExistsException(String name) {
        super(String.format("Course with name \"%s\" already exists.", name));
    }
}
