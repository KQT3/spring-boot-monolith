package chaincue.tech.jpabackend.masters.course_master

import chaincue.tech.jpabackend.masters.student_master.CourseHelper
import chaincue.tech.jpabackend.masters.student_master.StudentHelper
import chaincue.tech.jpabackend.masters.teacher_master.TeacherHelper
import chaincue.tech.jpabackend.utilities.ANSIColors
import org.junit.jupiter.api.Test

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.Commit
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
class CourseHelperTest(
        @Autowired val courseHelper: CourseHelper,
        @Autowired val teacherHelper: TeacherHelper,
        @Autowired val studentHelper: StudentHelper,
) {

    @Test
    fun save() {
        val result = courseHelper.save("name")
        ANSIColors.printBlue(result)
    }

    @Test
    @Transactional
    @Commit
    fun findStudent() {
        val course = courseHelper.save("name")
        val result = courseHelper.find(course.id)
        ANSIColors.printBlue(result.get())
    }

    @Test
    @Transactional
    @Commit
    fun addTeacherToCourse() {
        val course = courseHelper.save("names")
        val teacher = teacherHelper.save("name")
        val result = courseHelper.addTeacherToCourse(course.id, teacher.id)
        ANSIColors.printBlue(result)
    }

    @Test
    @Transactional
    @Commit
    fun addStudentToCourse() {
        val course = courseHelper.save("name")
        val student = studentHelper.save("name")
        val result = courseHelper.addStudentToCourse(course.id, student.id)
        ANSIColors.printBlue(result)
    }
}
