package chaincue.tech.r2dbcbackend2.masters.tag_master;

import chaincue.tech.r2dbcbackend2.masters.material_master.Material;
import chaincue.tech.r2dbcbackend2.masters.material_master.MaterialHelper;
import chaincue.tech.r2dbcbackend2.utilities.ANSIColors;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TagHelperTest {
    @Autowired
    TagHelper tagHelper;
    @Autowired
    MaterialHelper materialHelper;
    @Test
    void saveSuccess() {
        var tag = tagHelper.save("name").block();
        ANSIColors.printBlue(tag);
    }
    @Test
    void findTagByIdSuccess() {
        //given
        var tag = tagHelper.save("name").block();

        //when
        var result = tagHelper.findByTagId(tag.getId()).block();
        ANSIColors.printBlue(result);
    }
    @Test
    void addTagToMaterialSuccess() {
        //given
        var tag = tagHelper.save("name").block();
        var material = materialHelper.save("", Material.MaterialType.PDF, "").block();

        //when
        var result = tagHelper.addTagToMaterial(tag.getId(), material.getId()).block();
        ANSIColors.printBlue(result);
    }
}
