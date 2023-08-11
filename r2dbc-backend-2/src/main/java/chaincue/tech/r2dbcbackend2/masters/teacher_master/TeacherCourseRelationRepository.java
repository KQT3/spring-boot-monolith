package chaincue.tech.r2dbcbackend2.masters.teacher_master;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface TeacherCourseRelationRepository extends ReactiveCrudRepository<TeacherCourseRelation, String> {
    Flux<TeacherCourseRelation> findByTeacherId(String teacherId);
    Flux<TeacherCourseRelation> findByCourseId(String courseId);
}
