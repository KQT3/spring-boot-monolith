package chaincue.tech.jpabackend.masters.teacher_master;

import chaincue.tech.jpabackend.masters.user_master.User;
import chaincue.tech.jpabackend.masters.user_master.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@AllArgsConstructor
public class TeacherHelper {
    private final TeacherRepository teacherRepository;
    private final UserRepository userRepository;

    @Transactional
    public Teacher saveTeacher(String userName) {
        User user = User.createUser(userName);
        Teacher teacher = Teacher.createTeacher(userName);
        teacher.setUser(user);
        user.setTeacher(teacher);
        userRepository.save(user);
        return teacherRepository.save(teacher);
    }

    public Optional<Teacher> findTeacher(String id) {
        return teacherRepository.findById(id);
    }
}
