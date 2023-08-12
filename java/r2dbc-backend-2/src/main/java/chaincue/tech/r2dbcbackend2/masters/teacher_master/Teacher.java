package chaincue.tech.r2dbcbackend2.masters.teacher_master;

import chaincue.tech.r2dbcbackend2.masters.DomainObject;
import chaincue.tech.r2dbcbackend2.masters.course_master.Course;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.relational.core.mapping.Table;

import java.util.List;
import java.util.UUID;

@Table("teachers")
@AllArgsConstructor
@Data
public class Teacher implements DomainObject {
    @Id
    String id;
    String version;
    String name;
    int events;
    String userId;
    @ReadOnlyProperty
    List<Course> courses;

    public static Teacher create(String name) {
        return new Teacher(
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                name,
                0,
                null,
                List.of()
        );
    }
}
