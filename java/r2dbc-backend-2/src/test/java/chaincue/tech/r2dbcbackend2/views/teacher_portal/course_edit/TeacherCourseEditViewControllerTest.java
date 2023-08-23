package chaincue.tech.r2dbcbackend2.views.teacher_portal.course_edit;

import chaincue.tech.r2dbcbackend2.masters.assignment_master.AssignmentService;
import chaincue.tech.r2dbcbackend2.masters.course_master.Course;
import chaincue.tech.r2dbcbackend2.masters.course_master.CourseService;
import chaincue.tech.r2dbcbackend2.masters.material_master.Material;
import chaincue.tech.r2dbcbackend2.masters.material_master.MaterialService;
import chaincue.tech.r2dbcbackend2.masters.student_master.StudentService;
import chaincue.tech.r2dbcbackend2.masters.tag_master.TagService;
import chaincue.tech.r2dbcbackend2.masters.teacher_master.TeacherService;
import chaincue.tech.r2dbcbackend2.masters.unit_master.UnitService;
import chaincue.tech.r2dbcbackend2.utilities.JWTDecoderUtil;
import chaincue.tech.r2dbcbackend2.views.teacher_portal.course_edit.DTOs.*;
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

import java.util.Arrays;

import static chaincue.tech.r2dbcbackend2.utilities.ANSIColors.printBlue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("test")
@Testcontainers
@SpringBootTest
class TeacherCourseEditViewControllerTest {
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
    TeacherCourseEditViewController teacherCourseEditViewController;
    @Autowired
    TeacherService teacherService;
    @Autowired
    CourseService courseService;
    @Autowired
    UnitService unitService;
    @Autowired
    AssignmentService assignmentService;
    @Autowired
    TagService tagService;
    @Autowired
    MaterialService materialService;
    @Autowired
    StudentService studentService;

    @BeforeEach
    void setUp() {
        postgresContainer.start();
    }

    @Test
    void courseEditViewSuccess() {
        //Given
        var teacher = teacherService.save("", "", "").block();
        var fakeToken = JWTDecoderUtil.createToken(teacher.getId());
        var course = courseService.save("").block();
        tagService.save("").block();
        var material = materialService.save("material1", Material.MaterialType.PDF, "").block();
        materialService.save("material2", Material.MaterialType.PDF, "").block();
        var student1 = studentService.save("student1", "", "").block();
        studentService.save("student2", "", "").block();
        var unit1 = unitService.save("unit1").block();
        unitService.save("unit2").block();
        assignmentService.save("", unit1.getId()).block();

        unitService.addUnitToCourse(unit1.getId(), course.getId()).block();
        studentService.addStudentToCourse(student1.getId(), course.getId()).block();
        courseService.addCourseToMaterial(course.getId(), material.getId()).block();

        // When
        var createCourseDTO = teacherCourseEditViewController.courseEditView(fakeToken, course.getId()).block();
        printBlue(createCourseDTO);

        // Then
        assertEquals(course.getId(), createCourseDTO.id());
        assertTrue(createCourseDTO.allUnits().length >= 2);
        assertTrue(createCourseDTO.allMaterials().length >= 2);
        assertTrue(createCourseDTO.allStudents().length >= 2);
        assertEquals(1, createCourseDTO.units().length);
        assertEquals(1, createCourseDTO.studentsAdded().length);
        assertEquals(1, createCourseDTO.materials().length);
        assertEquals(1, Arrays.stream(createCourseDTO.allUnits()).filter(TeacherCourseEditDTO.Unit::attachedToCourse).toList().size());
    }

    @Test
    void searchMaterial() {
        // Given
        var teacher = teacherService.save("", "", "").block();
        var fakeToken = JWTDecoderUtil.createToken(teacher.getId());
        var course = courseService.save("").block();
        materialService.save("material1", Material.MaterialType.PDF, "").block();
        materialService.save("material2", Material.MaterialType.PDF, "").block();
        materialService.save("material3", Material.MaterialType.PDF, "").block();

        String[] searchValues = {"material1", "material2"};
        var teacherCourseEditSearchValues = new TeacherCourseEditSearchValues(searchValues);

        // When
        var materials = teacherCourseEditViewController.searchMaterial(fakeToken, course.getId(), teacherCourseEditSearchValues).collectList().block();

        // Then
        assertEquals(2, materials.size());
    }

    @Test
    void searchUnitSuccess() {
        // Given
        var teacher = teacherService.save("", "", "").block();
        var fakeToken = JWTDecoderUtil.createToken(teacher.getId());
        var course = courseService.save("").block();
        unitService.save("unit1").block();
        unitService.save("unit2").block();
        unitService.save("unit3").block();

        String[] searchValues = {"unit1", "unit2"};
        TeacherCourseEditSearchValues teacherCourseEditSearchValues = new TeacherCourseEditSearchValues(searchValues);

        // When
        var units = teacherCourseEditViewController.searchUnit(fakeToken, course.getId(), teacherCourseEditSearchValues).collectList().block();

        // Then
        assertTrue(units.stream().anyMatch(unit  -> unit.name().equals("unit1")));
        assertTrue(units.stream().anyMatch(unit -> unit.name().equals("unit2")));
    }

    @Test
    void searchStudentSuccess() {
        // Given
        var teacher = teacherService.save("", "", "").block();
        var fakeToken = JWTDecoderUtil.createToken(teacher.getId());
        var course = courseService.save("").block();
        studentService.save("student1", "", "").block();
        studentService.save("student2", "", "").block();
        studentService.save("student3", "", "").block();

        String[] searchValues = {"student1", "student2"};
        TeacherCourseEditSearchValues teacherCourseEditSearchValues = new TeacherCourseEditSearchValues(searchValues);

        // When
        var students = teacherCourseEditViewController.searchStudent(fakeToken, course.getId(), teacherCourseEditSearchValues).collectList().block();

        // Then
        assertTrue(students.stream().anyMatch(student -> student.name().equals("student1")));
        assertTrue(students.stream().anyMatch(student -> student.name().equals("student2")));
    }

    @Test
    void updateCourseTitleSuccess() {
        // Given
        String expectedValue = "newValue";
        var teacher = teacherService.save("", "", "").block();
        var fakeToken = JWTDecoderUtil.createToken(teacher.getId());
        var course = courseService.save("").block();
        var requestBody = new TeacherUpdateCourseRequestBody(course.getId(), expectedValue, Course.ChangeFieldName.TITLE);

        // When
        var createCourseDTO = teacherCourseEditViewController.updateCourseField(fakeToken, requestBody).block();

        // Then
        assertEquals(expectedValue, createCourseDTO.courseName());
    }

    @Test
    void updateDescriptionSuccess() {
        //Given
        String expectedValue = "newValue";
        var teacher = teacherService.save("", "", "").block();
        var fakeToken = JWTDecoderUtil.createToken(teacher.getId());
        var course = courseService.save("").block();
        var requestBody = new TeacherUpdateCourseRequestBody(course.getId(), expectedValue, Course.ChangeFieldName.DESCRIPTION);

        // When
        var createCourseDTO = teacherCourseEditViewController.updateCourseField(fakeToken, requestBody).block();

        // Then
        assertEquals(expectedValue, createCourseDTO.courseDescription());
    }

    @Test
    void updateStartDateSuccess() {
        //Given
        final String expectedValue = "08-19-2023";
        var teacher = teacherService.save("", "", "").block();
        var fakeToken = JWTDecoderUtil.createToken(teacher.getId());
        var course = courseService.save("").block();
        var requestBody = new TeacherUpdateCourseRequestBody(course.getId(), expectedValue, Course.ChangeFieldName.START_DATE);

        // When
        var createCourseDTO = teacherCourseEditViewController.updateCourseField(fakeToken, requestBody).block();

        // Then
        assertEquals("08/19/2023", createCourseDTO.startDate());
    }

    @Test
    void updateEndDateSuccess() {
        //Given
        final String expectedValue = "08-19-2023";
        var teacher = teacherService.save("", "", "").block();
        var fakeToken = JWTDecoderUtil.createToken(teacher.getId());
        var course = courseService.save("").block();
        var requestBody = new TeacherUpdateCourseRequestBody(course.getId(), expectedValue, Course.ChangeFieldName.END_DATE);

        // When
        var createCourseDTO = teacherCourseEditViewController.updateCourseField(fakeToken, requestBody).block();

        // Then
        assertEquals("08/19/2023", createCourseDTO.endDate());
    }

    @Test
    void updateStatusSuccess() {
        //Given
        final var expectedValue = Course.Status.COMPLETE;
        var teacher = teacherService.save("", "", "").block();
        var fakeToken = JWTDecoderUtil.createToken(teacher.getId());
        var course = courseService.save("").block();
        var requestBody = new TeacherUpdateCourseRequestBody(course.getId(), expectedValue.toString(), Course.ChangeFieldName.STATUS);

        // When
        var createCourseDTO = teacherCourseEditViewController.updateCourseField(fakeToken, requestBody).block();

        // Then
        assertEquals(expectedValue.toString(), createCourseDTO.status());
    }

    @Test
    void updateUnitsAddToCourseSuccess() {
        // Given
        final var expectedValue = Course.Status.COMPLETE;
        var teacher = teacherService.save("", "", "").block();
        var fakeToken = JWTDecoderUtil.createToken(teacher.getId());
        var course = courseService.save("").block();
        var unit = unitService.save("").block();
        var requestBody = new TeacherUpdateUnitsRequestBody(course.getId(), unit.getId());

        // When
        var createCourseDTO = teacherCourseEditViewController.addUnitsToCourse(fakeToken, requestBody).block();

        // Then
        assertEquals(1, createCourseDTO.units().length);
        assertTrue(createCourseDTO.units()[0].attachedToCourse());
    }

    @Test
    void removeUnitsFromCourseSuccess() {
        // Given
        var teacher = teacherService.save("", "", "").block();
        var fakeToken = JWTDecoderUtil.createToken(teacher.getId());
        var course = courseService.save("").block();
        var unit = unitService.save("").block();
        unitService.addUnitToCourse(unit.getId(), course.getId()).block();

        var requestBody = new TeacherUpdateUnitsRequestBody(course.getId(), unit.getId());

        // When
        var createCourseDTO = teacherCourseEditViewController.removeUnitsFromCourse(fakeToken, requestBody).block();

        // Then
        assertEquals(0, createCourseDTO.units().length);
    }

    @Test
    void deleteCourseSuccess() {
        // Given
        var teacher = teacherService.save("", "", "").block();
        var fakeToken = JWTDecoderUtil.createToken(teacher.getId());
        var course = courseService.save("").block();

        // When
        var courseId = teacherCourseEditViewController.deleteCourse(fakeToken, course.getId()).block();

        // Then
        assertEquals(course.getId(), courseId);
    }

    @Test
    void addCourseToMaterialSuccess() {
        // Given
        var teacher = teacherService.save("", "", "").block();
        var fakeToken = JWTDecoderUtil.createToken(teacher.getId());
        var course = courseService.save("course").block();
        var material = materialService.save("", Material.MaterialType.PDF, "").block();
        var requestBody = new TeacherUpdateMaterialRequestBody(course.getId(), material.getId());

        // When
        var teacherCourseEditDTO = teacherCourseEditViewController.addCourseToMaterial(fakeToken, requestBody).block();

        // Then
        assertEquals(1, teacherCourseEditDTO.materials().length);
        assertTrue(teacherCourseEditDTO.materials()[0].attachedToCourse());
    }

    @Test
    void addStudentsToCourseSuccess() {
        // Given
        var teacher = teacherService.save("", "", "").block();
        var fakeToken = JWTDecoderUtil.createToken(teacher.getId());
        var course = courseService.save("").block();
        var student = studentService.save("", "", "").block();
        var requestBody = new TeacherUpdateStudentRequestBody(course.getId(), student.getId());
        // When
        var teacherCourseEditDTO = teacherCourseEditViewController.addStudentsToCourse(fakeToken, requestBody).block();

        // Then
        assertEquals(1, teacherCourseEditDTO.studentsAdded().length);
        assertTrue(teacherCourseEditDTO.studentsAdded()[0].attachedToCourse());
    }

    @Test
    void removeStudentsFromCourseSuccess() {
        // Given
        var teacher = teacherService.save("", "", "").block();
        var fakeToken = JWTDecoderUtil.createToken(teacher.getId());
        var course = courseService.save("").block();
        var student = studentService.save("", "", "").block();
        studentService.addStudentToCourse(student.getId(), course.getId()).block();
        var requestBody = new TeacherUpdateStudentRequestBody(course.getId(), student.getId());

        // When
        var teacherCourseEditDTO = teacherCourseEditViewController.removeStudentsFromCourse(fakeToken, requestBody).block();

        // Then
        assertEquals(0, teacherCourseEditDTO.studentsAdded().length);
    }

    @Test
    void removeMaterialFromCourseSuccess() {
        // Given
        var teacher = teacherService.save("", "", "").block();
        var fakeToken = JWTDecoderUtil.createToken(teacher.getId());
        var course = courseService.save("course").block();
        var material = materialService.save("", Material.MaterialType.PDF, "").block();
        var block = courseService.addCourseToMaterial(course.getId(), material.getId()).block();
        var requestBody = new TeacherUpdateMaterialRequestBody(course.getId(), material.getId());

        // When
        var teacherCourseEditDTO = teacherCourseEditViewController.removeCourseFromMaterial(fakeToken, requestBody).block();

        //then
        assertEquals(0, teacherCourseEditDTO.materials().length);
    }

    @Test
    void duplicateCourse() {
    }
}
