package chaincue.tech.r2dbcbackend.masters.user_master;

import chaincue.tech.r2dbcbackend.masters.teacher_master.TeacherHelper;
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
class UserHelperTest {

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

    @Autowired
    UserHelper userHelper;

    @Test
    void saveUserSuccess() {
        String username = "user@email";
        User user = User.createUser(username);

        User result = userHelper.saveUser(user).block();

        ANSIColors.printBlue(result);
        Assertions.assertEquals(username, result.getUserName());
    }
}
