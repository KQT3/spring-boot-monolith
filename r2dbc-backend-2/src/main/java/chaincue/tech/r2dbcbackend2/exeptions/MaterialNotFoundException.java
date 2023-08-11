package chaincue.tech.r2dbcbackend2.exeptions;

public class MaterialNotFoundException extends RuntimeException {
    public MaterialNotFoundException(String id) {
        super(String.format("Material not found. Id: %s", id));
    }
}
