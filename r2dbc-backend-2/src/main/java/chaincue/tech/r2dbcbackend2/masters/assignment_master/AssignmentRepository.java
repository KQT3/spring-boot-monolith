package chaincue.tech.r2dbcbackend2.masters.assignment_master;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssignmentRepository extends ReactiveCrudRepository<Assignment, String> {
}
