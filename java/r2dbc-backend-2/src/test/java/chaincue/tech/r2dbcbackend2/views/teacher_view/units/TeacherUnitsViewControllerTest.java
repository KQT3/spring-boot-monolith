package chaincue.tech.r2dbcbackend2.views.teacher_view.units;

import chaincue.tech.r2dbcbackend2.masters.course_master.CourseService;
import chaincue.tech.r2dbcbackend2.masters.tag_master.TagService;
import chaincue.tech.r2dbcbackend2.masters.teacher_master.TeacherService;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ActiveProfiles("test")
@Testcontainers
@SpringBootTest
class TeacherUnitsViewControllerTest {
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
    TeacherUnitsViewController teacherUnitsViewController;
    @Autowired
    TagService tagService;
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
    void unitsViewSuccess() {
        //given
        var teacher = teacherService.save("", "", "").block();
        var fakeToken = JWTDecoderUtil.createToken(teacher.getId());
        var unit = unitService.save("").block();
        var course = courseService.save("").block();
        var tag = tagService.save("").block();
        unitService.save("").block();
        unitService.save("").block();
        unitService.addUnitToCourse(unit.getId(), course.getId()).block();
        unitService.addUnitToTag(unit.getId(), tag.getId()).block();

        //when
        TeacherUnitsViewDTO unitsViewDTO = teacherUnitsViewController.unitsView(fakeToken).block();

        //when
        assertEquals(3, unitsViewDTO.units().length);
    }

    @Test
    void searchUnitViewSuccess() {
        //given
        var teacher = teacherService.save("", "", "").block();
        var fakeToken = JWTDecoderUtil.createToken(teacher.getId());
        var unit = unitService.save("unit1").block();
        var tag = tagService.save("").block();
        unitService.save("unit2").block();
        unitService.save("unit3").block();
        unitService.addUnitToTag(unit.getId(), tag.getId()).block();
        String[] searchValues = {"unit1", "unit2"};
        var teacherFilterUnitsDTO = new TeacherFilterUnitsDTO(searchValues);

        //when
        var unitFiltered = teacherUnitsViewController.searchUnitView(fakeToken, teacherFilterUnitsDTO).collectList().block();

        //then
        assertEquals(2, unitFiltered.size());
    }

    @Test
    void createUnitSuccess() {
        //given
        var teacher = teacherService.save("", "", "").block();
        var fakeToken = JWTDecoderUtil.createToken(teacher.getId());

        //when
        var unitId = teacherUnitsViewController.createUnit(fakeToken, "name").block();

        //then
        assertNotNull(unitId);

    }
}
