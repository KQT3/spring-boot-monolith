package chaincue.tech.r2dbcbackend2.views.teacher_portal.assignments;

import chaincue.tech.r2dbcbackend2.masters.assignment_master.AssignmentService;
import chaincue.tech.r2dbcbackend2.masters.course_master.CourseService;
import chaincue.tech.r2dbcbackend2.masters.teacher_master.TeacherService;
import chaincue.tech.r2dbcbackend2.masters.unit_master.UnitService;
import chaincue.tech.r2dbcbackend2.utilities.JWTDecoderUtil;
import chaincue.tech.r2dbcbackend2.views.teacher_portal.assignments.DTOs.TeacherCreateAssignmentReqBody;
import chaincue.tech.r2dbcbackend2.views.teacher_portal.assignments.DTOs.TeacherFilterAssignmentDTO;
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
class TeacherAssignmentsViewControllerTest {
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
    TeacherAssignmentsViewController teacherAssignmentsViewController;
    @Autowired
    CourseService courseService;
    @Autowired
    TeacherService teacherService;
    @Autowired
    UnitService unitService;
    @Autowired
    AssignmentService assignmentService;

    @BeforeEach
    void setUp() {
        postgresContainer.start();
    }

    @Test
    void assignmentsViewSuccess() {
        //given
        var teacher = teacherService.save("", "", "").block();
        var fakeToken = JWTDecoderUtil.createToken(teacher.getId());
        courseService.save("").block();
        var unit = unitService.save("").block();
        assignmentService.save("", unit.getId()).block();

        //when
        var assignmentsViewDTO = teacherAssignmentsViewController.assignmentsView(fakeToken).block();

        //then
        assertEquals(1, assignmentsViewDTO.courses().length);
        assertTrue(assignmentsViewDTO.assignments().length >= 1);
    }

    @Test
    void createAssignmentSuccess() {
        //given
        var teacher = teacherService.save("", "", "").block();
        var fakeToken = JWTDecoderUtil.createToken(teacher.getId());
        var teacherCreateAssignmentReqBody = new TeacherCreateAssignmentReqBody("assignment", "");

        //when
        var assignmentId = teacherAssignmentsViewController.createAssignment(fakeToken, teacherCreateAssignmentReqBody).block();

        //then
        assertNotNull(assignmentId);
    }

    @Test
    void duplicateAssignmentSuccess() {
    }

    @Test
    void searchAssignmentsViewSuccess() {
        //given
        var teacher = teacherService.save("", "", "").block();
        var fakeToken = JWTDecoderUtil.createToken(teacher.getId());

        var unit1 = unitService.save("").block();
        assignmentService.save("assignment1", unit1.getId()).block();
        var unit2 = unitService.save("").block();
        assignmentService.save("assignment2", unit2.getId()).block();
        var unit3 = unitService.save("").block();
        assignmentService.save("assignment3", unit3.getId()).block();

        String[] searchValues = {"assignment1", "assignment2"};
        var teacherFilterCourseDTO = new TeacherFilterAssignmentDTO(searchValues);

        //when
        var result = teacherAssignmentsViewController.searchAssignmentsView(fakeToken, teacherFilterCourseDTO).collectList().block();

        //then
        assertEquals(2, result.size());
    }

}
