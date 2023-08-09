package chaincue.tech.r2dbcbackend.masters.course_master;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends ReactiveCrudRepository<Course, String> {
}
