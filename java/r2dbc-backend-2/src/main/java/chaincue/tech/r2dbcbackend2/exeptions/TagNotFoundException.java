package chaincue.tech.r2dbcbackend2.exeptions;

public class TagNotFoundException extends RuntimeException {
    public TagNotFoundException(String id) {
        super(String.format("Tag not found. Id: %s", id));
    }
}
