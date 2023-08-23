package chaincue.tech.r2dbcbackend2.views.teacher_portal.courses;

import chaincue.tech.r2dbcbackend2.masters.course_master.CourseService;
import chaincue.tech.r2dbcbackend2.masters.teacher_master.TeacherService;
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

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@Testcontainers
@SpringBootTest
class TeacherCoursesViewControllerTest {
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
    @Autowired
    TeacherCoursesViewController teacherCoursesViewController;
    @Autowired
    CourseService courseService;
    @Autowired
    TeacherService teacherService;

    @BeforeEach
    void setUp() {
        postgresContainer.start();
    }

    @Test
    void coursesView() {
        //given
        courseService.save("").block();
        courseService.save("").block();
        var course1 = courseService.save("").block();
        var course2 = courseService.save("").block();
        var teacher = teacherService.save("", "", "").block();
        teacherService.addTeacherToCourse(teacher.getId(), course1.getId()).block();
        teacherService.addTeacherToCourse(teacher.getId(), course2.getId()).block();
        var fakeToken = JWTDecoderUtil.createToken(teacher.getId());

        //when
        TeacherCoursesViewDTO coursesViewDTO = teacherCoursesViewController.coursesView(fakeToken).block();

        //then
        assertTrue(coursesViewDTO.allCourses().length >= 4);
        assertTrue(coursesViewDTO.myCourses().length >= 4);
    }

    @Test
    void createCourse() {
        //given
        var teacher = teacherService.save("", "", "").block();
        var fakeToken = JWTDecoderUtil.createToken(teacher.getId());

        //when
        var courseId = teacherCoursesViewController.createCourse(fakeToken, "courseName").block();

        //then
        assertNotNull(courseId);
    }

    @Test
    void searchCoursesView() {
        //given
        courseService.save("course1").block();
        courseService.save("course2").block();
        courseService.save("course3").block();
        var teacher = teacherService.save("", "", "").block();
        var fakeToken = JWTDecoderUtil.createToken(teacher.getId());
        String[] searchValues = {"course1", "course2"};
        var teacherFilterCourseDTO = new TeacherFilterCourseDTO(searchValues);

        //when
        var result = teacherCoursesViewController.searchCoursesView(fakeToken, teacherFilterCourseDTO).collectList().block();

        //then
        assertEquals(2, result.size());
    }
}
