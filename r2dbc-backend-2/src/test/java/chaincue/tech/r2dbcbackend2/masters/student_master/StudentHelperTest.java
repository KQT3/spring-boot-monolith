package chaincue.tech.r2dbcbackend2.masters.student_master;

import chaincue.tech.r2dbcbackend2.masters.course_master.CourseHelper;
import chaincue.tech.r2dbcbackend2.utilities.ANSIColors;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static chaincue.tech.r2dbcbackend2.utilities.ANSIColors.printBlue;

@SpringBootTest
class StudentHelperTest {
    @Autowired
    StudentHelper studentHelper;
    @Autowired
    CourseHelper courseHelper;

    @Test
    void saveStudentSuccess() {
        Student.create("");
        var student = studentHelper.save("student@gmail.com", "student", "student").block();
        printBlue(student);
        Assertions.assertEquals("student@gmail.com", student.getName());
        Assertions.assertNotNull(student.getUserId());
    }

    @Test
    void addStudentToCourseSuccess() {
        //given
        var course = courseHelper.save("name").block();
        var student = studentHelper.save("", "", "").block();

        //when
        var result = studentHelper.addStudentToCourse(student.getId(), course.getId()).block();
        printBlue(result);
    }

    @Test
    void findStudentByIdWithCoursesSuccess() {
        //given
        var course1 = courseHelper.save("name").block();
        var course2 = courseHelper.save("name").block();
        var student = studentHelper.save("student@gmail.com", "student", "student").block();
        studentHelper.addStudentToCourse(student.getId(), course1.getId()).block();
        studentHelper.addStudentToCourse(student.getId(), course2.getId()).block();

        //when
        var result = studentHelper.findStudentByIdWithCourses(student.getId()).block();
        printBlue(result);
    }

}
