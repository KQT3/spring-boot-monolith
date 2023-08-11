package chaincue.tech.r2dbcbackend2.masters.assignment_master;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class AssignmentHelper {
    private final AssignmentRepository assignmentRepository;

    @Transactional
    public Mono<Assignment> save(String name, String unitId) {
        var assignment = Assignment.create(name, unitId);
        return assignmentRepository.save(assignment);
    }
}
