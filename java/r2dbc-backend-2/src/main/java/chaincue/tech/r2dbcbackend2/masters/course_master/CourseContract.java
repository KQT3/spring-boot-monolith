package chaincue.tech.r2dbcbackend2.masters.course_master;

import chaincue.tech.r2dbcbackend2.masters.material_master.MaterialRelation;
import chaincue.tech.r2dbcbackend2.masters.unit_master.Unit;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CourseContract {
    Mono<Course> save(String name);

    Mono<Course> findCourseByIdWithRelations(String courseId);

    Flux<Course> findAllWithRelations();

    Flux<Course> findAllWithRelations(Unit unit);

    Mono<Course> findCourseById(String course);

    Mono<MaterialRelation> addCourseToMaterial(String courseId, String materialId);

    Mono<MaterialRelation> removeCourseFromMaterial(String courseId, String materialId);

    @Transactional
    Mono<String> deleteById(String id);

    @Transactional
    Mono<Course> updateCourseField(String courseId, String courseTitle, Course.ChangeFieldName changeFieldName);
}
