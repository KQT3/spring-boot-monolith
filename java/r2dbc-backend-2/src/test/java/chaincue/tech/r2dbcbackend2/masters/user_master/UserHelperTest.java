package chaincue.tech.r2dbcbackend2.masters.user_master;

import chaincue.tech.r2dbcbackend2.masters.teacher_master.TeacherHelper;
import chaincue.tech.r2dbcbackend2.utilities.ANSIColors;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.junit.jupiter.Container;

import java.io.File;

@SpringBootTest
//@ActiveProfiles("test")
//@Testcontainers
class UserHelperTest {

    @Container
    private static final DockerComposeContainer<?> serverContainer = new DockerComposeContainer<>(new File("src/main/resources/docker-compose-test.yml"));

    @Autowired
    TeacherHelper teacherHelper;
//
//    @BeforeEach
//    void setUp() {
//        serverContainer.start();
//    }
//
//    @AfterEach
//    void tearDown() {
//        serverContainer.stop();
//    }

    @Autowired
    UserHelper userHelper;

    @Test
    void saveUserSuccess() {
        String username = "user@email";
        String firstname = "firstname";
        String lastname = "lastname";
        User user = User.createUser(username, firstname, lastname);

        User result = userHelper.saveUser(user).block();

        ANSIColors.printBlue(result);
        Assertions.assertEquals(username, result.getUserName());
        Assertions.assertEquals(firstname, result.getFirstName());
        Assertions.assertEquals(lastname, result.getLastName());
    }
}
