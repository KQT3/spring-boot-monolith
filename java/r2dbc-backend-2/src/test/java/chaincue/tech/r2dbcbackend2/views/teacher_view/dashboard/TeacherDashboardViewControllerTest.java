package chaincue.tech.r2dbcbackend2.views.teacher_view.dashboard;

import chaincue.tech.r2dbcbackend2.masters.course_master.Course;
import chaincue.tech.r2dbcbackend2.masters.course_master.CourseService;
import chaincue.tech.r2dbcbackend2.masters.student_master.StudentService;
import chaincue.tech.r2dbcbackend2.masters.teacher_master.TeacherService;
import chaincue.tech.r2dbcbackend2.masters.unit_master.Unit;
import chaincue.tech.r2dbcbackend2.masters.unit_master.UnitService;
import chaincue.tech.r2dbcbackend2.utilities.JWTDecoderUtil;
import org.junit.jupiter.api.BeforeEach;
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

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;


@ActiveProfiles("test")
@Testcontainers
@SpringBootTest
class TeacherDashboardViewControllerTest {
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

    String fakeToken = JWTDecoderUtil.createToken(UUID.randomUUID().toString());
    @Autowired
    TeacherDashboardViewController teacherDashboardViewController;
    @Autowired
    StudentService studentService;
    @Autowired
    CourseService courseService;
    @Autowired
    TeacherService teacherService;
    @Autowired
    UnitService unitService;

    @BeforeEach
    void setUp() {
        postgresContainer.start();
    }

    @Test
    void dashboardViewSuccess() {
        //given
        studentService.save("", "", "").block();
        teacherService.save("", "", "").block();
        Course course = courseService.save("").block();
        Unit unit = unitService.save("").block();
        unitService.addUnitToCourse(unit.getId(), course.getId()).block();

        //when
        var result = teacherDashboardViewController.dashboardView(fakeToken).block();

        //then
        assertEquals("", result.name());
        assertEquals(1, result.students().length);
        assertEquals(2, result.teachers().length);
        assertEquals(1, result.courses().length);
        assertEquals(1, result.courses()[0].units().length);
    }
}
