package chaincue.tech.r2dbcbackend2.masters.unit_master.repositories;

import chaincue.tech.r2dbcbackend2.masters.unit_master.UnitCourseRelation;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface UnitCourseRelationRepository extends ReactiveCrudRepository<UnitCourseRelation, String> {
    Flux<UnitCourseRelation> findByUnitId(String unitId);

    Flux<UnitCourseRelation> findByCourseId(String courseId);
    Mono<UnitCourseRelation> findByUnitIdAndCourseId(String unitId, String courseId);

    Mono<Boolean> existsByUnitIdAndCourseId(String unitId, String courseId);

}
