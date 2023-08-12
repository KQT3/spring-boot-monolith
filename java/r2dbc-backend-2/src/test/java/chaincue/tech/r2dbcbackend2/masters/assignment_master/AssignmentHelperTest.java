package chaincue.tech.r2dbcbackend2.masters.assignment_master;

import chaincue.tech.r2dbcbackend2.masters.unit_master.UnitHelper;
import chaincue.tech.r2dbcbackend2.utilities.ANSIColors;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AssignmentHelperTest {
    @Autowired
    AssignmentHelper assignmentHelper;
    @Autowired
    UnitHelper unitHelper;

    @Test
    void saveSuccess() {
        var unit = unitHelper.save("unit").block();
        Assignment assignment = assignmentHelper.save("name", unit.getId()).block();
        ANSIColors.printBlue(assignment);
    }
}
