package chaincue.tech.r2dbcbackend2.masters.student_master;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface StudentCourseRelationRepository extends ReactiveCrudRepository<StudentCourseRelation, String> {
    Flux<StudentCourseRelation> findByStudentId(String teacherId);
    Flux<StudentCourseRelation> findByCourseId(String courseId);
}
