package chaincue.tech.r2dbcbackend2.masters.material_master.repositories;

import chaincue.tech.r2dbcbackend2.masters.material_master.MaterialRelation;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface MaterialRelationsRepository extends ReactiveCrudRepository<MaterialRelation, String> {
    Flux<MaterialRelation> findByMaterialId(String materialId);

    Flux<MaterialRelation> findByCourseId(String courseId);

    Flux<MaterialRelation> findByUnitId(String unitId);

    Flux<MaterialRelation> findByAssignmentId(String assignmentId);

    Flux<MaterialRelation> findByTagId(String tagId);

    Mono<MaterialRelation> findByMaterialIdAndCourseId(String materialId, String courseId);

    Mono<Boolean> existsByMaterialIdAndCourseId(String materialId, String courseId);

    Mono<Boolean> existsByMaterialIdAndUnitId(String materialId, String unitId);

    Mono<Boolean> existsByMaterialIdAndAssignmentId(String materialId, String assignmentId);
}
