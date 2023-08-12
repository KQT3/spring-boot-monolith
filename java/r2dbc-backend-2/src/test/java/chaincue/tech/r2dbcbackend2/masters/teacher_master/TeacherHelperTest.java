package chaincue.tech.r2dbcbackend2.masters.teacher_master;

import chaincue.tech.r2dbcbackend2.masters.course_master.CourseHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.junit.jupiter.Container;

import java.io.File;

import static chaincue.tech.r2dbcbackend2.utilities.ANSIColors.printBlue;


@SpringBootTest
//@ActiveProfiles("test")
//@Testcontainers
class TeacherHelperTest {

    @Container
    private static final DockerComposeContainer<?> serverContainer = new DockerComposeContainer<>(new File("src/main/resources/docker-compose-test.yml"));

    @Autowired
    TeacherHelper teacherHelper;
    @Autowired
    CourseHelper courseHelper;

//    @BeforeEach
//    void setUp() {
//        serverContainer.start();
//    }
//
//    @AfterEach
//    void tearDown() {
//        serverContainer.stop();
//    }

    @Test
    void saveTeacherSuccess() {
        Teacher teacher = teacherHelper.save("teacher@gmail.com", "teacher", "teacher").block();
        printBlue(teacher);
        Assertions.assertEquals("teacher@gmail.com", teacher.getName());
        Assertions.assertNotNull(teacher.getUserId());
    }

    @Test
    void getTeacherByIdSuccess() {
        Teacher teacher = teacherHelper.save("teacher@gmail.com", "teacher", "teacher").block();

        Teacher result = teacherHelper.getTeacherById(teacher.getId()).block();
        System.out.println(result);
    }

    @Test
    void getTeacherByIdWithCoursesSuccess() {
        //given
        var course1 = courseHelper.save("name").block();
        var course2 = courseHelper.save("name").block();
        Teacher teacher = teacherHelper.save("teacher@gmail.com", "teacher", "teacher").block();
        teacherHelper.addTeacherToCourse(teacher.getId(), course1.getId()).block();
        teacherHelper.addTeacherToCourse(teacher.getId(), course2.getId()).block();

        //when
        Teacher result = teacherHelper.findTeacherByIdWithCourses(teacher.getId()).block();
        printBlue(result);
    }

    @Test
    void addTeacherToCourseSuccess() {
        //given
        var course = courseHelper.save("name").block();
        var teacher = teacherHelper.save("", "", "").block();

        //when
        var result = teacherHelper.addTeacherToCourse(teacher.getId(), course.getId()).block();
        printBlue(result);
    }

//    @Disabled
//    @Test
//    void getTeacherWithCoursesSuccess() {
//        var course1 = courseHelper.saveCourse("name").block();
//        var teacher1 = teacherHelper.saveTeacher("", "", "").block();
//        var teacher2 = teacherHelper.saveTeacher("", "", "").block();
//        teacherHelper.addTeacherToCourse(teacher1.getId(), course1.getId()).block();
//        teacherHelper.addTeacherToCourse(teacher2.getId(), course1.getId()).block();
//
//        var result = courseHelper.findCourseByIdWithRelations(course1.getId()).block();
//        printBlue(result);
//    }
}
