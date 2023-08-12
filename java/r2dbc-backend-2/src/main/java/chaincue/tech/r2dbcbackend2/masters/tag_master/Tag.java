package chaincue.tech.r2dbcbackend2.masters.tag_master;

import chaincue.tech.r2dbcbackend2.masters.DomainObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.relational.core.mapping.Table;

import java.util.List;
import java.util.UUID;

@Table("tags")
@AllArgsConstructor
@Data
public class Tag implements DomainObject {
    @Id
    String id;
    String version;
    String name;
    @ReadOnlyProperty
    List<String> materialRelations;

    public static Tag create(String name) {
        return new Tag(
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                name,
                List.of()
        );
    }
}
