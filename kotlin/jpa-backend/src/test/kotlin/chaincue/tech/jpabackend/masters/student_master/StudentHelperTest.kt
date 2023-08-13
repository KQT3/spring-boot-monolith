package chaincue.tech.jpabackend.masters.student_master

import chaincue.tech.jpabackend.utilities.ANSIColors
import org.junit.jupiter.api.Test

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class StudentHelperTest(@Autowired val studentHelper: StudentHelper) {

    @Test
    fun saveSuccess() {
        val result = studentHelper.save("name")
        ANSIColors.printBlue(result)
    }

    @Test
    fun findStudent() {
        val student = studentHelper.save("name")
        val result = studentHelper.findStudent(student.id)
        ANSIColors.printBlue(result.get())
    }
}
