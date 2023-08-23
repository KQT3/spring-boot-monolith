package chaincue.tech.r2dbcbackend2.masters.student_master.repositories;

import chaincue.tech.r2dbcbackend2.masters.student_master.Student;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends ReactiveCrudRepository<Student, String> {
}
