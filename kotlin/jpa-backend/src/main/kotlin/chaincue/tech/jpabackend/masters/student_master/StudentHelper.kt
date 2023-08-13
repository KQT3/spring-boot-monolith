package chaincue.tech.jpabackend.masters.student_master

import chaincue.tech.jpabackend.masters.teacher_master.Student
import chaincue.tech.jpabackend.masters.user_master.User
import chaincue.tech.jpabackend.masters.user_master.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class StudentHelper(
        private val studentRepository: StudentRepository,
        private val userRepository: UserRepository
) {
    @Transactional
    fun save(username: String): Student {
        val user = User.create(username)
        val student = Student.create(username, user)
        user.student = student
        userRepository.save(user)
        return studentRepository.save(student)
    }

    fun findStudent(id: String): Optional<Student> = studentRepository.findById(id)
}
