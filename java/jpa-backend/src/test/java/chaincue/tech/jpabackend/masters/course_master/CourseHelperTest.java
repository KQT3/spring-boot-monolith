package chaincue.tech.jpabackend.masters.course_master;

import chaincue.tech.jpabackend.masters.student_master.StudentHelper;
import chaincue.tech.jpabackend.masters.teacher_master.TeacherHelper;
import chaincue.tech.jpabackend.utilities.ANSIColors;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class CourseHelperTest {
    @Autowired
    CourseHelper courseHelper;
    @Autowired
    TeacherHelper teacherHelper;
    @Autowired
    StudentHelper studentHelper;

    @Test
    void saveCourseSuccess() {
        var name = courseHelper.saveCourse("name");
        ANSIColors.printBlue(name.getName());
    }

    @Test
    void addTeacherToCourse() {
        //given
        var course = courseHelper.saveCourse("courseName");
        var teacher = teacherHelper.saveTeacher("teacherName");

        //when
        var result = courseHelper.addTeacherToCourse(course.getId(), teacher.getId());
        ANSIColors.printBlue(result);
    }

    @Test
    void addStudentToCourse() {
        //given
        var course = courseHelper.saveCourse("courseName");
        var student = studentHelper.saveStudent("studentName");

        //when
        Course result= courseHelper.addStudentToCourse(course.getId(), student.getId());
        ANSIColors.printBlue(result);
    }

    @Test
    @Transactional
    void getCourseWithTeachers() {
        //given
        var course = courseHelper.saveCourse("courseName");
        var teacher = teacherHelper.saveTeacher("studentName");

        //when
        Course courseWithStudent = courseHelper.addTeacherToCourse(course.getId(), teacher.getId());

        //then
        var result = courseHelper.getCourseById(courseWithStudent.getId()).get();
        ANSIColors.printPurple(result.getTeacherCourseRelations());
    }

    @Test
    @Transactional
    void getCourseWithStudent() {
        //given
        var course = courseHelper.saveCourse("courseName");
        var student1 = studentHelper.saveStudent("studentName");
        var student2 = studentHelper.saveStudent("studentName");

        //when
        courseHelper.addStudentToCourse(course.getId(), student1.getId());
        courseHelper.addStudentToCourse(course.getId(), student2.getId());

        //then
        var result = courseHelper.getCourseById(course.getId()).get();
        ANSIColors.printPurple(result.getStudentCourseRelations());
    }

}
