package chaincue.tech.r2dbcbackend2.masters.unit_master;

import chaincue.tech.r2dbcbackend2.masters.DomainObject;
import chaincue.tech.r2dbcbackend2.masters.assignment_master.Assignment;
import chaincue.tech.r2dbcbackend2.masters.course_master.Course;
import chaincue.tech.r2dbcbackend2.masters.tag_master.Tag;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.UUID;

@Table("units")
@AllArgsConstructor
@Data
public class Unit implements DomainObject {
    @NonNull
    @Id
    String id;
    String version;
    String name;
    String description;
    int events;
    @ReadOnlyProperty
    List<Course> courses;
    @ReadOnlyProperty
    List<String> materialRelations;
    @ReadOnlyProperty
    List<Assignment> assignments;
    @ReadOnlyProperty
    List<Tag> tags;

    public static Unit create(String name) {
        return new Unit(
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                name,
                "",
                0,
                List.of(),
                List.of(),
                List.of(),
                List.of()
        );
    }

    public enum ChangeFieldName {
        TITLE,
        DESCRIPTION
    }
}
