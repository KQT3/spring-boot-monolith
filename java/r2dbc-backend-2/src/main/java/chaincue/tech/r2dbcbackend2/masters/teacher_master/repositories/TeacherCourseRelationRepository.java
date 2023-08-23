package chaincue.tech.r2dbcbackend2.masters.teacher_master.repositories;

import chaincue.tech.r2dbcbackend2.masters.teacher_master.TeacherCourseRelation;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface TeacherCourseRelationRepository extends ReactiveCrudRepository<TeacherCourseRelation, String> {
    Flux<TeacherCourseRelation> findByTeacherId(String teacherId);
    Flux<TeacherCourseRelation> findByCourseId(String courseId);

    Mono<Boolean> existsByTeacherIdAndCourseId(String teacherId, String courseId);
}
