package chaincue.tech.r2dbcbackend2.utilities;

import chaincue.tech.r2dbcbackend2.masters.assignment_master.Assignment;
import chaincue.tech.r2dbcbackend2.masters.assignment_master.AssignmentService;
import chaincue.tech.r2dbcbackend2.masters.course_master.CourseService;
import chaincue.tech.r2dbcbackend2.masters.material_master.Material;
import chaincue.tech.r2dbcbackend2.masters.material_master.MaterialService;
import chaincue.tech.r2dbcbackend2.masters.student_master.StudentService;
import chaincue.tech.r2dbcbackend2.masters.teacher_master.TeacherService;
import chaincue.tech.r2dbcbackend2.masters.unit_master.Unit;
import chaincue.tech.r2dbcbackend2.masters.unit_master.UnitService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "local")
public class DefaultDataForLocalDevelopment {
    boolean defaultData;

    @Bean
    ApplicationRunner applicationRunner(
            TeacherService teacherService,
            StudentService studentService,
            MaterialService materialService,
            UnitService unitService,
            CourseService courseService,
            AssignmentService assignmentService
    ) {
        return args -> {
            log.info("defaultData: " + defaultData);
            if (defaultData) {
                teacherService.save("s", "", "").subscribe();
                studentService.save("s", "", "").subscribe();
                materialService.save("s", Material.MaterialType.PDF, "").subscribe();
                courseService.save("s").subscribe();
                Assignment assignment = assignmentService.save("s", null).block();
                Unit unit = unitService.save("s").block();
                assignmentService.attachAssignmentToUnit(assignment.getId(), unit.getId()).subscribe();
            }
        };
    }
}
