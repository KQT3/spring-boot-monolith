package chaincue.tech.r2dbcbackend2.masters.unit_master.repositories;

import chaincue.tech.r2dbcbackend2.masters.unit_master.Unit;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface UnitRepository extends ReactiveCrudRepository<Unit, String> {
    @Modifying
    @Query("UPDATE units SET name = :name WHERE id = :id")
    Mono<Integer> updateUnitName(String id, String name);

    @Modifying
    @Query("UPDATE units SET description = :name WHERE id = :id")
    Mono<Integer> updateUnitDescription(String id, String description);
}
