package chaincue.tech.r2dbcbackend2.masters.teacher_master;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeacherRepository extends ReactiveCrudRepository<Teacher, String> {
}
