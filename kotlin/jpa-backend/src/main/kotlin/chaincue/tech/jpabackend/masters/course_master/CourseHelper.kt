package chaincue.tech.jpabackend.masters.student_master

import chaincue.tech.jpabackend.masters.teacher_master.Course
import chaincue.tech.jpabackend.masters.teacher_master.TeacherRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class CourseHelper(
        private val courseRepository: CourseRepository,
        private val teacherRepository: TeacherRepository,
        private val studentRepository: StudentRepository
) {
    fun save(username: String): Course {
        val course = Course.create(username)
        return courseRepository.save(course)
    }

    fun find(id: String): Optional<Course> = courseRepository.findById(id)

    @Transactional
    fun addTeacherToCourse(courseId: String, teacherId: String): Course {
        val course = courseRepository.findById(courseId).orElseThrow { RuntimeException("Course or Teacher not found.") }
        val teacher = teacherRepository.findById(teacherId).orElseThrow { RuntimeException("Course or Teacher not found.") }
        course.teacherCourseRelations.add(teacher)
        teacher.teacherCourseRelations.add(course)

        courseRepository.save(course)
        teacherRepository.save(teacher)

        return course
    }

//    @Transactional
//    fun addStudentToCourse(courseId: String, studentId: String): Course {
//        val course = courseRepository.findById(courseId).orElseThrow { RuntimeException("Course not found.") }
//        val student = studentRepository.findById(studentId).orElseThrow { RuntimeException("Student not found.") }
//
////        course.studentCourseRelations.add(student)
//        student.studentCourseRelations.add(course)
//
//        courseRepository.save(course)
//        studentRepository.save(student)
//
//        return course
//    }
}
