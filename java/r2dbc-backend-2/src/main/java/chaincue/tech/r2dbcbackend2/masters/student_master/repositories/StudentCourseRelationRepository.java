package chaincue.tech.r2dbcbackend2.masters.student_master.repositories;

import chaincue.tech.r2dbcbackend2.masters.student_master.StudentCourseRelation;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface StudentCourseRelationRepository extends ReactiveCrudRepository<StudentCourseRelation, String> {
    Flux<StudentCourseRelation> findByStudentId(String teacherId);

    Flux<StudentCourseRelation> findByCourseId(String courseId);

    Mono<Boolean> existsByStudentIdAndCourseId(String studentId, String courseId);

    Mono<StudentCourseRelation> findByStudentIdAndCourseId(String studentId, String courseId);
}
