package chaincue.tech.r2dbcbackend2.masters.material_master;

import chaincue.tech.r2dbcbackend2.masters.DomainObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.lang.NonNull;

import java.util.UUID;

@Table("material_relations")
@AllArgsConstructor
@Data
public class MaterialRelation implements DomainObject {
    @NonNull
    @Id
    String id;
    @Column("material_id")
    String materialId;
    @Column("course_id")
    String courseId;
    @Column("unit_id")
    String unitId;
    @Column("assignment_id")
    String assignmentId;
    @Column("tag_id")
    String tagId;
    RelationType relationType;
    int events;

    public static MaterialRelation create(String materialId, String relationId, RelationType relationType) {
        switch (relationType) {
            case COURSE -> {
                return new MaterialRelation(UUID.randomUUID().toString(), materialId, relationId, null, null, null, relationType, 0);
            }
            case UNIT -> {
                return new MaterialRelation(UUID.randomUUID().toString(), materialId, null, relationId, null, null, relationType, 0);
            }
            case ASSIGNMENT -> {
                return new MaterialRelation(UUID.randomUUID().toString(), materialId, null, null, relationId, null, relationType, 0);
            }
            case TAG -> {
                return new MaterialRelation(UUID.randomUUID().toString(), materialId, null, null, null, relationId, relationType, 0);
            }
        }
        throw new RuntimeException("RelationType dont exist: " + relationType);
    }

    public enum RelationType {
        COURSE,
        UNIT,
        ASSIGNMENT,
        TAG,
    }
}
