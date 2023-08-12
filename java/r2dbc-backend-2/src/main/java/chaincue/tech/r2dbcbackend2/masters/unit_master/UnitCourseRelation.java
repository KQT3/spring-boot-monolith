package chaincue.tech.r2dbcbackend2.masters.unit_master;

import chaincue.tech.r2dbcbackend2.masters.DomainObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Data
@Table(name = "unit_course_relations")
@AllArgsConstructor
public class UnitCourseRelation implements DomainObject {
    @Id
    String id;
    String version;
    @Column("unit_id")
    String unitId;
    @Column("course_id")
    String courseId;
    String name;
    int events;

    public static UnitCourseRelation create(String unitId, String courseId) {
        return new UnitCourseRelation(
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                unitId,
                courseId,
                "name",
                0);
    }
}
