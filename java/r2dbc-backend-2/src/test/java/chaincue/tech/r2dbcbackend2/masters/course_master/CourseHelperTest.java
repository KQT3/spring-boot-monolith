package chaincue.tech.r2dbcbackend2.masters.course_master;

import chaincue.tech.r2dbcbackend2.masters.material_master.Material;
import chaincue.tech.r2dbcbackend2.masters.material_master.MaterialHelper;
import chaincue.tech.r2dbcbackend2.masters.student_master.StudentHelper;
import chaincue.tech.r2dbcbackend2.masters.teacher_master.TeacherHelper;
import chaincue.tech.r2dbcbackend2.masters.unit_master.UnitHelper;
import chaincue.tech.r2dbcbackend2.utilities.ANSIColors;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.junit.jupiter.Container;

import java.io.File;

@Disabled
@SpringBootTest
//@ActiveProfiles("test")
//@Testcontainers
class CourseHelperTest {
    @Container
    private static final DockerComposeContainer<?> serverContainer = new DockerComposeContainer<>(new File("src/main/resources/docker-compose-test.yml"));

    @Autowired
    CourseHelper courseHelper;
    @Autowired
    TeacherHelper teacherHelper;
    @Autowired
    StudentHelper studentHelper;
    @Autowired
    UnitHelper unitHelper;
    @Autowired
    MaterialHelper materialHelper;

//    @BeforeEach
//    void setUp() {
//        serverContainer.start();
//    }
//
//    @AfterEach
//    void tearDown() {
//        serverContainer.stop();
//    }

    @Test
    void saveCourseSuccess() {
        var name = courseHelper.save("name").block();
        ANSIColors.printBlue(name);
        Assertions.assertEquals("name", name.getName());
    }

    @Test
    void addCourseToMaterialSuccess() {
        //given
        var material = materialHelper.save("name", Material.MaterialType.PDF, "uri").block();
        var course = courseHelper.save("").block();

        //when
        var result = courseHelper.addCourseToMaterial(course.getId(), material.getId()).block();
        ANSIColors.printBlue(result);
    }

    @Test
    void findCourseByIdSuccess() {
        //given
        var courseCreated = courseHelper.save("name").block();

        var course = courseHelper.findCourseById(courseCreated.getId()).block();
        ANSIColors.printBlue(course);
    }

    @Test
    void findCourseByIdWithRelationsSuccess() {
        //given
        var courseCreated = courseHelper.save("name").block();
        var teacher1 = teacherHelper.save("teacher1", "", "").block();
        var teacher2 = teacherHelper.save("teacher2", "", "").block();
        var student1 = studentHelper.save("student1", "", "").block();
        var student2 = studentHelper.save("student2", "", "").block();
        var unit1 = unitHelper.save("unit1").block();
        var unit2 = unitHelper.save("unit2").block();
        teacherHelper.addTeacherToCourse(teacher1.getId(), courseCreated.getId()).block();
        teacherHelper.addTeacherToCourse(teacher2.getId(), courseCreated.getId()).block();
        studentHelper.addStudentToCourse(student1.getId(), courseCreated.getId()).block();
        studentHelper.addStudentToCourse(student2.getId(), courseCreated.getId()).block();
        unitHelper.addUnitToCourse(unit1.getId(),courseCreated.getId()).block();
        unitHelper.addUnitToCourse(unit2.getId(),courseCreated.getId()).block();

        //when
        var course = courseHelper.findCourseByIdWithRelations(courseCreated.getId()).block();
        ANSIColors.printBlue(course);
    }

    @Test
    void findCourseByIdWithMaterialsSuccess() {
        //given
        var course = courseHelper.save("unit").block();
        var material = materialHelper.save("name", Material.MaterialType.PDF, "uri").block();
        courseHelper.addCourseToMaterial(course.getId(), material.getId()).block();

        //when
        var result = courseHelper.findCourseByIdWithRelations(course.getId()).block();
        ANSIColors.printBlue(result);
        Assertions.assertNotNull(result.getMaterialRelations());
    }
}
