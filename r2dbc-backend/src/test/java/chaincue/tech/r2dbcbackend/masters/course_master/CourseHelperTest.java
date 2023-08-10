package chaincue.tech.r2dbcbackend.masters.course_master;

import chaincue.tech.r2dbcbackend.masters.teacher_master.TeacherHelper;
import chaincue.tech.r2dbcbackend.utilities.ANSIColors;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.File;
import java.util.Random;

@Disabled
@SpringBootTest
//@ActiveProfiles("test")
//@Testcontainers
class CourseHelperTest {
    @Container
    private static final DockerComposeContainer<?> serverContainer = new DockerComposeContainer<>(new File("src/main/resources/docker-compose-test.yml"));

    @Autowired
    CourseHelper courseHelper;
    @Autowired
    TeacherHelper teacherHelper;

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
    void saveCourseSuccess() {
        Random rand = new Random();
        var name = courseHelper.createCourse("name" + rand.nextInt(1000)).block();
        ANSIColors.printBlue(name);
    }

    @Test
    void getCourseSuccess() {
        var course = courseHelper.saveCourse("result").block();

        var result = courseHelper.findById(course.getId()).block();
        ANSIColors.printBlue(result);
    }

//    @Test
//    void addTeacherToCourse() {
//        //given
//        var course = courseHelper.saveCourse("name").block();
//        var teacher = teacherHelper.saveTeacher("").block();
//
//        //when
//        var name = courseHelper.addTeacherToCourse(course.getId(), teacher.getId()).block();
//        ANSIColors.printBlue(name);
//
//        //then
//        Assertions.assertEquals("name", name.getName());
//    }
}
