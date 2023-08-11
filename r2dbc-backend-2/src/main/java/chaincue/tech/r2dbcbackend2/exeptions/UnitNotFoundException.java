package chaincue.tech.r2dbcbackend2.exeptions;

public class UnitNotFoundException extends RuntimeException {
    public UnitNotFoundException(String id) {
        super(String.format("Unit not found. Id: %s", id));
    }
}
