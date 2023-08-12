package chaincue.tech.r2dbcbackend.exeptions;

public class CourseAlreadyExistsException extends RuntimeException {
    public CourseAlreadyExistsException(String name) {
        super(String.format("Course with name \"%s\" already exists.", name));
    }
}
