package chaincue.tech.r2dbcbackend2.masters.material_master;

import chaincue.tech.r2dbcbackend2.masters.DomainObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Data
@Table(name = "material_tag_relations")
@AllArgsConstructor
public class MaterialTagRelation implements DomainObject {
    @Id
    String id;
    String version;
    @Column("material_id")
    String materialId;
    @Column("tag_id")
    String tagId;

    public static MaterialTagRelation create(String materialId, String tagId) {
        return new MaterialTagRelation(
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                materialId,
                tagId);
    }
}
