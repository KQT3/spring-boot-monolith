package chaincue.tech.jpabackend.masters.student_master;

import chaincue.tech.jpabackend.masters.teacher_master.Teacher;
import chaincue.tech.jpabackend.masters.user_master.User;
import chaincue.tech.jpabackend.masters.user_master.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@AllArgsConstructor
public class StudentHelper {
    private final StudentRepository studentRepository;
    private final UserRepository userRepository;

    public Student saveStudent(String userName) {
        var user = User.createUser(userName);
        var student = Student.createStudent(userName);
        student.setUser(user);
        user.setStudent(student);
        userRepository.save(user);
        return studentRepository.save(student);
    }
}
