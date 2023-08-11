package chaincue.tech.r2dbcbackend2.masters.material_master;

import chaincue.tech.r2dbcbackend2.masters.course_master.CourseHelper;
import chaincue.tech.r2dbcbackend2.utilities.ANSIColors;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MaterialHelperTest {
    @Autowired
    MaterialHelper materialHelper;
    @Autowired
    CourseHelper courseHelper;

    @Test
    void saveSuccess() {
        var name = materialHelper.save("name", Material.MaterialType.PDF, "uri").block();
        ANSIColors.printBlue(name);
    }


    @Test
    void findMaterialByIdSuccess() {
        var material = materialHelper.save("name", Material.MaterialType.PDF, "uri").block();

        var name = materialHelper.findMaterialById(material.getId()).block();
        ANSIColors.printBlue(name);
    }

    @Test
    void findMaterialByIdWithRelationsSuccess() {
        //given
        var material = materialHelper.save("name", Material.MaterialType.PDF, "uri").block();
        var course1 = courseHelper.save("").block();
        var course2 = courseHelper.save("").block();
        courseHelper.addCourseToMaterial(course1.getId(), material.getId()).block();
        courseHelper.addCourseToMaterial(course2.getId(), material.getId()).block();

        //when
        var name = materialHelper.findMaterialByIdWithRelations(material.getId()).block();
        ANSIColors.printBlue(name);
    }


}
