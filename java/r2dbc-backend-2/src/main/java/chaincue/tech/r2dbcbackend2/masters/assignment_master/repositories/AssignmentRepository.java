package chaincue.tech.r2dbcbackend2.masters.assignment_master.repositories;

import chaincue.tech.r2dbcbackend2.masters.assignment_master.Assignment;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface AssignmentRepository extends ReactiveCrudRepository<Assignment, String> {
    @Modifying
    @Query("UPDATE assignments SET unit_id = :unitId WHERE id = :id")
    Mono<Integer> attachAssignmentToUnit(String id, String unitId);

    @Query("SELECT * FROM assignments WHERE unit_id = :unitId")
    Flux<Assignment> findAllByUnitId(String unitId);
}
