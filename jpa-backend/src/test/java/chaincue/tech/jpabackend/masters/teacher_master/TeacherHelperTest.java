package chaincue.tech.jpabackend.masters.teacher_master;

import chaincue.tech.jpabackend.utilities.ANSIColors;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.junit.jupiter.Container;

import java.io.File;

@SpringBootTest
//@ActiveProfiles("test")
//@Testcontainers
class TeacherHelperTest {

    @Container
    private static final DockerComposeContainer<?> serverContainer = new DockerComposeContainer<>(new File("src/main/resources/docker-compose-test.yml"));

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
    void saveTeacherSuccess() {
        Teacher teacher = teacherHelper.saveTeacher("teacher@gmail.com");
        ANSIColors.printBlue(teacher);
        ANSIColors.printBlue(teacher.getUser());
        Assertions.assertEquals("teacher@gmail.com", teacher.getName());
    }

    @Test
    @Transactional
    void findTeacherById() {
        Teacher teacher = teacherHelper.saveTeacher("teacher@gmail.com");

        var optionalTeacher = teacherHelper.findTeacher(teacher.getId()).get();
        ANSIColors.printBlue(optionalTeacher);

        Assertions.assertEquals("teacher@gmail.com", teacher.getName());
    }
}
