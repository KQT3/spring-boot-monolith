package chaincue.tech.r2dbcbackend2.views.teacher_portal.materials;

import chaincue.tech.r2dbcbackend2.masters.assignment_master.AssignmentService;
import chaincue.tech.r2dbcbackend2.masters.course_master.CourseService;
import chaincue.tech.r2dbcbackend2.masters.material_master.Material;
import chaincue.tech.r2dbcbackend2.masters.material_master.MaterialService;
import chaincue.tech.r2dbcbackend2.masters.tag_master.TagService;
import chaincue.tech.r2dbcbackend2.masters.teacher_master.TeacherService;
import chaincue.tech.r2dbcbackend2.masters.unit_master.UnitService;
import chaincue.tech.r2dbcbackend2.utilities.JWTDecoderUtil;
import chaincue.tech.r2dbcbackend2.views.teacher_portal.materials.DTOs.TeacherSearchValues;
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

import static chaincue.tech.r2dbcbackend2.utilities.ANSIColors.printBlue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("test")
@Testcontainers
@SpringBootTest
class TeacherMaterialsViewControllerTest {
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
    TeacherMaterialsViewController teacherMaterialsViewController;
    @Autowired
    MaterialService materialService;
    @Autowired
    CourseService courseService;
    @Autowired
    TeacherService teacherService;
    @Autowired
    TagService tagService;
    @Autowired
    AssignmentService assignmentService;
    @Autowired
    UnitService unitService;

    @BeforeEach
    void setUp() {
        postgresContainer.start();
    }

    @Test
    void materialViewSuccess() {
        //given
        var teacher = teacherService.save("", "", "").block();
        var fakeToken = JWTDecoderUtil.createToken(teacher.getId());
        var unit = unitService.save("").block();
        var assignment = assignmentService.save("", unit.getId()).block();
        var course = courseService.save("").block();
        courseService.save("").block();
        var tag = tagService.save("").block();
        var material = materialService.save("", Material.MaterialType.PDF, "").block();

        courseService.addCourseToMaterial(course.getId(), material.getId()).block();
        assignmentService.addAssignmentToMaterial(assignment.getId(), material.getId()).block();
        unitService.addUnitToMaterial(unit.getId(), material.getId()).block();
        tagService.addTagToMaterial(tag.getId(), material.getId()).block();

        //when
        var materialsViewDTO = teacherMaterialsViewController.materialView(fakeToken).block();
        printBlue(materialsViewDTO);

        //then
        assertEquals(2, materialsViewDTO.allCourses().length);
        assertEquals(1, materialsViewDTO.allAssignments().length);
        assertEquals(1, materialsViewDTO.allTags().length);
        assertEquals(1, materialsViewDTO.allUnits().length);
        assertTrue(materialsViewDTO.allMaterials().length >= 1);
        assertEquals(1, materialsViewDTO.allMaterials()[0].materialCourseRelations().length);
        assertEquals(1, materialsViewDTO.allMaterials()[0].materialAssignmentRelations().length);
        assertEquals(1, materialsViewDTO.allMaterials()[0].materialUnitRelations().length);
        assertEquals(1, materialsViewDTO.allMaterials()[0].tags().length);
    }

    @Test
    void searchMaterialSuccess() {
        //given
        var teacher = teacherService.save("", "", "").block();
        var fakeToken = JWTDecoderUtil.createToken(teacher.getId());
        materialService.save("material1", Material.MaterialType.PDF, "").block();
        materialService.save("material2", Material.MaterialType.PDF, "").block();
        materialService.save("material3", Material.MaterialType.PDF, "").block();

        String[] searchValues = {"material1", "material2"};
        TeacherSearchValues teacherSearchValues = new TeacherSearchValues(searchValues);

        //when
        var materialsViewDTO = teacherMaterialsViewController.searchMaterial(fakeToken, teacherSearchValues).block();
        assertEquals(2, materialsViewDTO.allMaterials().length);
    }

}
