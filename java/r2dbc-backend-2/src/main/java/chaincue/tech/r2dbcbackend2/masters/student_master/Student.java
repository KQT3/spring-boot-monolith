package chaincue.tech.r2dbcbackend2.masters.student_master;

import chaincue.tech.r2dbcbackend2.masters.DomainObject;
import chaincue.tech.r2dbcbackend2.masters.course_master.Course;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.UUID;

@Table("students")
@AllArgsConstructor
@Data
public class Student implements DomainObject {
    @Id
    @NonNull
    String id;
    String version;
    String name;
    int events;
    String userId;
    @ReadOnlyProperty
    List<Course> courses;

    public static Student create(String name){
        return new Student(
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                name,
                0,
                null,
                List.of()
        );
    }
}
