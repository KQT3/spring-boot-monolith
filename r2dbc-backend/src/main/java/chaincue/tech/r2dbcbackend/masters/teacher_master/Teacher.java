package chaincue.tech.r2dbcbackend.masters.teacher_master;

import chaincue.tech.r2dbcbackend.masters.DomainObject;
import chaincue.tech.r2dbcbackend.masters.course_master.Course;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Table;

import java.util.List;
import java.util.UUID;

@Table("teachers")
@AllArgsConstructor
@Data
public class Teacher implements DomainObject {
    @Id
    String id;
    String name;
    String userId;
    @Transient
    List<Course> courses;

    public static Teacher createTeacher(String name) {
        return new Teacher(UUID.randomUUID().toString(), name, null, List.of());
    }
}
