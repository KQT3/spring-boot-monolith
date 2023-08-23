package chaincue.tech.r2dbcbackend2.masters.teacher_master.repositories;

import chaincue.tech.r2dbcbackend2.masters.teacher_master.Teacher;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeacherRepository extends ReactiveCrudRepository<Teacher, String> {
}
