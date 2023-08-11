package chaincue.tech.r2dbcbackend2.masters.unit_master;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface UnitCourseRelationRepository extends ReactiveCrudRepository<UnitCourseRelation, String> {
    Flux<UnitCourseRelation> findByUnitId(String unitId);
    Flux<UnitCourseRelation> findByCourseId(String courseId);
}
