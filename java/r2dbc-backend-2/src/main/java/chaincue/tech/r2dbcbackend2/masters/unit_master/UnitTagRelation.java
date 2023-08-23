package chaincue.tech.r2dbcbackend2.masters.unit_master;

import chaincue.tech.r2dbcbackend2.masters.DomainObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Data
@Table(name = "unit_tag_relations")
@AllArgsConstructor
public class UnitTagRelation implements DomainObject {
    @Id
    String id;
    String version;
    @Column("unit_id")
    String unitId;
    @Column("tag_id")
    String tagId;

    public static UnitTagRelation create(String unitId, String tagId) {
        return new UnitTagRelation(
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                unitId,
                tagId);
    }
}
