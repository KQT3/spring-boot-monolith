package chaincue.tech.r2dbcbackend2.masters.course_master;

import chaincue.tech.r2dbcbackend2.masters.DomainObject;
import chaincue.tech.r2dbcbackend2.masters.student_master.Student;
import chaincue.tech.r2dbcbackend2.masters.teacher_master.Teacher;
import chaincue.tech.r2dbcbackend2.masters.unit_master.Unit;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Table("courses")
@AllArgsConstructor
@Data
public class Course implements DomainObject {
    @Id
    String id;
    String version;
    String name;
    String description;
    int events;
    String schoolName;
    Status status;
    LocalDate startDate;
    LocalDate endDate;
    @ReadOnlyProperty
    List<Teacher> teachers;
    @ReadOnlyProperty
    List<Student> students;
    @ReadOnlyProperty
    List<Unit> units;
    @ReadOnlyProperty
    List<String> materialRelations;

    public enum Status {
        WIP,
        ACTIVE,
        COMPLETE,
        INACTIVE
    }

    public static Course create(String name) {
        return new Course(
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                name,
                "",
                0,
                "",
                Status.WIP,
                LocalDate.now(),
                LocalDate.now(),
                List.of(),
                List.of(),
                List.of(),
                List.of()
        );
    }
}
