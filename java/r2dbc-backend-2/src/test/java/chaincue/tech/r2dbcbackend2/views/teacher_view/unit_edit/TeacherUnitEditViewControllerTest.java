package chaincue.tech.r2dbcbackend2.views.teacher_view.unit_edit;

import chaincue.tech.r2dbcbackend2.masters.assignment_master.AssignmentService;
import chaincue.tech.r2dbcbackend2.masters.course_master.CourseService;
import chaincue.tech.r2dbcbackend2.masters.material_master.Material;
import chaincue.tech.r2dbcbackend2.masters.material_master.MaterialService;
import chaincue.tech.r2dbcbackend2.masters.tag_master.TagService;
import chaincue.tech.r2dbcbackend2.masters.teacher_master.TeacherService;
import chaincue.tech.r2dbcbackend2.masters.unit_master.Unit;
import chaincue.tech.r2dbcbackend2.masters.unit_master.UnitService;
import chaincue.tech.r2dbcbackend2.utilities.JWTDecoderUtil;
import chaincue.tech.r2dbcbackend2.views.teacher_view.unit_edit.DTOs.TeacherUpdateUnitFieldDTO;
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

@ActiveProfiles("test")
@Testcontainers
@SpringBootTest
class TeacherUnitEditViewControllerTest {
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
    TeacherUnitEditViewController teacherUnitEditViewController;
    @Autowired
    TeacherService teacherService;
    @Autowired
    UnitService unitService;
    @Autowired
    AssignmentService assignmentService;
    @Autowired
    TagService tagService;
    @Autowired
    CourseService courseService;
    @Autowired
    MaterialService materialService;

    @Test
    void createUnitFormView() {
        // Given
        var teacher = teacherService.save("", "", "").block();
        var fakeToken = JWTDecoderUtil.createToken(teacher.getId());
        var unit = unitService.save("").block();
        var assignment1 = assignmentService.save("assignment", unit.getId()).block();
        var assignment2 = assignmentService.save("assignment", null).block();
        var material1 = materialService.save("material", Material.MaterialType.BOOK, "").block();
        var material2 = materialService.save("material", Material.MaterialType.BOOK, "").block();
        var course1 = courseService.save("course").block();
        var course2 = courseService.save("course").block();
        var tag1 = tagService.save("tag").block();
        var tag2 = tagService.save("tag").block();

        unitService.addUnitToCourse(unit.getId(), course1.getId()).block();
        unitService.addUnitToMaterial(unit.getId(), material1.getId()).block();
        unitService.addUnitToTag(unit.getId(), tag1.getId()).block();

        // When
        var editDTO = teacherUnitEditViewController.unitEditView(fakeToken, unit.getId()).block();

        // Then
        assertEquals(1, editDTO.assignments().length);
        assertEquals(1, editDTO.courses().length);
        assertEquals(1, editDTO.materials().length);
        assertEquals(2, editDTO.allMaterials().length);
        assertEquals(1, editDTO.tagsAttached().length);
        assertEquals(1, editDTO.allTags().length);
    }

    @Test
    void updateUnitTitle() {
        // Given
        String expectedValue = "newValue";
        var teacher = teacherService.save("", "", "").block();
        var fakeToken = JWTDecoderUtil.createToken(teacher.getId());
        var unit = unitService.save("").block();
        var requestBody = new TeacherUpdateUnitFieldDTO(unit.getId(), expectedValue, Unit.ChangeFieldName.TITLE);

        // When
        var createCourseDTO = teacherUnitEditViewController.updateUnitField(fakeToken, requestBody).block();

        // Then
        assertEquals(expectedValue, createCourseDTO.name());
    }

    @Test
    void updateUnitDescription() {
        // Given
        String expectedValue = "newValue";
        var teacher = teacherService.save("", "", "").block();
        var fakeToken = JWTDecoderUtil.createToken(teacher.getId());
        var unit = unitService.save("").block();
        var requestBody = new TeacherUpdateUnitFieldDTO(unit.getId(), expectedValue, Unit.ChangeFieldName.DESCRIPTION);

        // When
        var createCourseDTO = teacherUnitEditViewController.updateUnitField(fakeToken, requestBody).block();

        // Then
        assertEquals(expectedValue, createCourseDTO.description());
    }

    @Test
    void updateUnitRemoveAssignment() {
    }

    @Test
    void addTag() {
    }

    @Test
    void removeTag() {
    }

    @Test
    void addUnitToMaterial() {
    }

    @Test
    void removeMaterialsFromUnit() {
    }

    @Test
    void searchMaterial() {
    }

    @Test
    void createAssignment() {
    }
}
