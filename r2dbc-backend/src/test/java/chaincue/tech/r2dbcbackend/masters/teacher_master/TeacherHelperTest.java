package chaincue.tech.r2dbcbackend.masters.teacher_master;

import chaincue.tech.r2dbcbackend.utilities.ANSIColors;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.File;

@Disabled
@SpringBootTest
//@ActiveProfiles("test")
//@Testcontainers
class TeacherHelperTest {

    @Container
    private static final DockerComposeContainer<?> serverContainer = new DockerComposeContainer<>(new File("src/main/resources/docker-compose-test.yml"));

    @Autowired
    TeacherHelper teacherHelper;

    @BeforeEach
    void setUp() {
        serverContainer.start();
    }

    @AfterEach
    void tearDown() {
        serverContainer.stop();
    }

    @Test
    void saveTeacherSuccess() {
        Teacher teacher = teacherHelper.saveTeacher("teacher@gmail.com").block();
        ANSIColors.printBlue(teacher);
        Assertions.assertEquals("teacher@gmail.com", teacher.getName());
        Assertions.assertNotNull(teacher.getUserId());
    }
}
