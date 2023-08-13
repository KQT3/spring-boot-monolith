package chaincue.tech.jpabackend.masters.teacher_master

import chaincue.tech.jpabackend.masters.user_master.User
import chaincue.tech.jpabackend.masters.user_master.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class TeacherHelper(
        private val teacherRepository: TeacherRepository,
        private val userRepository: UserRepository
) {
    @Transactional
    fun save(username: String): Teacher {
        val user = User.create(username)
        val teacher = Teacher.create(username, user)
        user.teacher = teacher
        userRepository.save(user)
        return teacherRepository.save(teacher)
    }

    fun findTeacher(id: String): Optional<Teacher> = teacherRepository.findById(id)
}
