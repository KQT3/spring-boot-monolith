package chaincue.tech.r2dbcbackend2.masters.unit_master;

import chaincue.tech.r2dbcbackend2.masters.course_master.CourseHelper;
import chaincue.tech.r2dbcbackend2.masters.material_master.Material;
import chaincue.tech.r2dbcbackend2.masters.material_master.MaterialHelper;
import chaincue.tech.r2dbcbackend2.utilities.ANSIColors;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UnitHelperTest {
    @Autowired
    UnitHelper unitHelper;
    @Autowired
    CourseHelper courseHelper;
    @Autowired
    MaterialHelper materialHelper;
    @Test
    void saveCourseSuccess() {
        var result = unitHelper.save("unit").block();
        System.out.println(result);
    }

    @Test
    void addUnitToCourseSuccess() {
        //given
        var unit = unitHelper.save("unit").block();
        var course = courseHelper.save("").block();

        //when
        var result = unitHelper.addUnitToCourse(unit.getId(), course.getId()).block();
        ANSIColors.printBlue(result);
    }

    @Test
    void addUnitToMaterialSuccess() {
        //given
        var material = materialHelper.save("name", Material.MaterialType.PDF, "uri").block();
        var unit = unitHelper.save("unit").block();

        //when
        var result = unitHelper.addUnitToMaterial(unit.getId(), material.getId()).block();
        ANSIColors.printBlue(result);
    }

    @Test
    void findUnitByIdWithCoursesSuccess() {
        //given
        var unit = unitHelper.save("unit").block();
        var course1 = courseHelper.save("").block();
        var course2 = courseHelper.save("").block();
        unitHelper.addUnitToCourse(unit.getId(), course1.getId()).block();
        unitHelper.addUnitToCourse(unit.getId(), course2.getId()).block();

        //when
        Unit result = unitHelper.findUnitByIdWithRelations(unit.getId()).block();
        ANSIColors.printBlue(result);
    }

    @Test
    void findUnitByIdWithMaterialsSuccess() {
        //given
        var unit = unitHelper.save("unit").block();
        var material = materialHelper.save("name", Material.MaterialType.PDF, "uri").block();
        unitHelper.addUnitToMaterial(unit.getId(), material.getId()).block();

        //when
        Unit result = unitHelper.findUnitByIdWithRelations(unit.getId()).block();
        ANSIColors.printBlue(result);
        Assertions.assertNotNull(result.getMaterialRelations());
    }
}
