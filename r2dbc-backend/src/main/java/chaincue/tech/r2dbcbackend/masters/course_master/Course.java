package chaincue.tech.r2dbcbackend.masters.course_master;

import chaincue.tech.r2dbcbackend.masters.DomainObject;
import chaincue.tech.r2dbcbackend.masters.teacher_master.Teacher;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
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
    String name;
    @Transient
    List<Teacher> teachers;
    //    List<String> studentCourseRelations;
//    List<String> teacherCourseRelations;

    public enum Status {
        WIP,
        ACTIVE,
        COMPLETE,
        INACTIVE
    }

    public static Course createCourse(String name) {
        return new Course(
                UUID.randomUUID().toString(),
                name,
                List.of()
        );
    }
}
