package chaincue.tech.jpabackend.masters.student_master;

import chaincue.tech.jpabackend.utilities.ANSIColors;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class StudentHelperTest {
    @Autowired
    StudentHelper studentHelper;

    @Test
    void saveTeacherSuccess() {
        var student = studentHelper.saveStudent("teacher@gmail.com");
        ANSIColors.printBlue(student);
        ANSIColors.printBlue(student.getUser());
    }
}
