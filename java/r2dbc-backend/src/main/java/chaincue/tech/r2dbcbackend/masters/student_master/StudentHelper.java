package chaincue.tech.r2dbcbackend.masters.student_master;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
public class StudentHelper {
    private final StudentRepository studentRepository;

    public StudentHelper(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Transactional
    public Mono<Student> saveStudent(Student student) {
        return studentRepository.save(student);
    }
}
