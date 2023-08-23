package chaincue.tech.r2dbcbackend2.masters.assignment_master;

import chaincue.tech.r2dbcbackend2.masters.material_master.MaterialRelation;
import chaincue.tech.r2dbcbackend2.masters.unit_master.Unit;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AssignmentContract {

    Mono<Assignment> save(String name, String unitId);

    Flux<Assignment> findAllWithRelations();

    Flux<Assignment> findAllWithRelations(Unit unit);

    @Transactional
    Mono<MaterialRelation> addAssignmentToMaterial(String assignmentId, String materialId);

    @Transactional
    Mono<Assignment> attachAssignmentToUnit(String assignmentId, String unitId);
}
