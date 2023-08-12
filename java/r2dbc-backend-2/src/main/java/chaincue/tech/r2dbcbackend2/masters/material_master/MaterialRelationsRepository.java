package chaincue.tech.r2dbcbackend2.masters.material_master;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface MaterialRelationsRepository extends ReactiveCrudRepository<MaterialRelation, String> {
    Flux<MaterialRelation> findByMaterialId(String materialId);
    Flux<MaterialRelation> findByCourseId(String courseId);
    Flux<MaterialRelation> findByUnitId(String unitId);
    Flux<MaterialRelation> findByAssignmentId(String assignmentId);
    Flux<MaterialRelation> findByTagId(String tagId);
}
