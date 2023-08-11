package chaincue.tech.r2dbcbackend2.masters;

import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;

public interface DomainObject extends Persistable<String> {

    default boolean createNewDomain() {
        return true;
    }

    @Override
    @Transient
    default boolean isNew() {
        return createNewDomain() || getId() == null;
    }
}
