package chaincue.tech.r2dbcbackend.masters.teacher_master;

import chaincue.tech.r2dbcbackend.masters.user_master.User;
import chaincue.tech.r2dbcbackend.masters.user_master.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
@Service
@AllArgsConstructor
public class TeacherHelper {
    private final TeacherRepository teacherRepository;
    private final UserRepository userRepository;

    @Transactional
    public Mono<Teacher> saveTeacher(String userName) {
        User user = User.createUser(userName);
        Teacher teacher = Teacher.createTeacher(userName);
        teacher.setUserId(user.getId());
        user.setTeacherId(teacher.getId());
        return userRepository.save(user).then(teacherRepository.save(teacher));
    }
}
