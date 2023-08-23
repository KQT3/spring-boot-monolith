package chaincue.tech.r2dbcbackend2.masters.unit_master;

import chaincue.tech.r2dbcbackend2.masters.course_master.Course;
import chaincue.tech.r2dbcbackend2.masters.material_master.MaterialRelation;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UnitContract {
    Mono<Unit> save(String name);

    Mono<UnitCourseRelation> addUnitToCourse(String unitId, String courseId);

    Mono<UnitCourseRelation> removeUnitFromCourse(String unitId, String courseId);

    Mono<UnitTagRelation> addUnitToTag(String unitId, String tagId);

    Mono<MaterialRelation> addUnitToMaterial(String unitId, String materialId);

    Mono<Unit> findUnitByIdWithRelations(String unitId);

    Flux<Unit> findAllWithRelations();

    Flux<Unit> findAllWithRelations(Course course);

    Flux<Unit> findByNameContainingIgnoreCase(String[] searchValues);

    @Transactional
    Mono<Unit> updateUnitField(String unitId, String newValue, Unit.ChangeFieldName changeFieldName);
}
