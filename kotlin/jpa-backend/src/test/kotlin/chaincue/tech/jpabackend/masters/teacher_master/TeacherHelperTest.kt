package chaincue.tech.jpabackend.masters.teacher_master

import chaincue.tech.jpabackend.utilities.ANSIColors
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class TeacherHelperTest(@Autowired val teacherHelper: TeacherHelper) {

    @Test
    fun saveSuccess() {
        val result = teacherHelper.save("name")
        ANSIColors.printBlue(result)
    }

    @Test
    fun findTeacherSuccess() {
        val teacher = teacherHelper.save("name")
        val result = teacherHelper.findTeacher(teacher.id)
        ANSIColors.printBlue(result.get())
    }
}
