package chaincue.tech.r2dbcbackend2.masters.unit_master;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UnitRepository extends ReactiveCrudRepository<Unit, String> {
}
