package chaincue.tech.r2dbcbackend2.views.teacher_view.root;

import chaincue.tech.r2dbcbackend2.utilities.JWTDecoderUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static chaincue.tech.r2dbcbackend2.utilities.ANSIColors.printBlue;

@SpringBootTest
@ActiveProfiles("test")
@Testcontainers
class TeacherRootViewControllerTest {
    @Container
    private static final PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>(DockerImageName.parse("postgres:15.3"))
            .withDatabaseName("postgres")
            .withUsername("admin")
            .withPassword("admin")
            .withClasspathResourceMapping("schemas", "/docker-entrypoint-initdb.d", BindMode.READ_ONLY);

    @DynamicPropertySource
    public static void registerPgProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.r2dbc.url", () -> "r2dbc:postgresql://localhost:"
                + postgresContainer.getFirstMappedPort()
                + "/postgres");
        registry.add("spring.r2dbc.username", () -> "admin");
        registry.add("spring.r2dbc.password", () -> "admin");
    }

    String fakeToken = JWTDecoderUtil.createToken("kajsa@home.se");

    @Autowired
    TeacherRootViewController teacherRootViewController;

    @Test
    void rootViewSuccess() {
        var rootDTO = teacherRootViewController.teacherRootView(fakeToken).block();
        printBlue(rootDTO);
    }

    @Test
    void markAllNotifyAsReadSuccess() {
    }
}
