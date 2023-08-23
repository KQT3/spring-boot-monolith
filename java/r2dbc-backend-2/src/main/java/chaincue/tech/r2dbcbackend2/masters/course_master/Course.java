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
import org.springframework.lang.NonNull;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Table("courses")
@AllArgsConstructor
@Data
public class Course implements DomainObject {
    @Id
    @NonNull
    private String id;
    private String version;
    @NonNull
    private String name;
    private String description;
    private int events;
    private String schoolName;
    private Status status;
    private LocalDate startDate;
    private LocalDate endDate;
    @ReadOnlyProperty
    private List<Teacher> teachers;
    @ReadOnlyProperty
    private List<Student> students;
    @ReadOnlyProperty
    private List<Unit> units;
    @ReadOnlyProperty
    List<String> materialRelations;

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

    public enum Status {
        WIP,
        ACTIVE,
        COMPLETE,
        INACTIVE
    }

    public enum ChangeFieldName {
        TITLE,
        DESCRIPTION,
        START_DATE,
        END_DATE,
        STATUS;
    }
}
