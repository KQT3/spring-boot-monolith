package chaincue.tech.r2dbcbackend2.masters.material_master;

import chaincue.tech.r2dbcbackend2.masters.DomainObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.UUID;

@Table("materials")
@AllArgsConstructor
@Data
public class Material implements DomainObject {
    @Id
    @NonNull
    String id;
    String version;
    String name;
    @NonNull MaterialType materialType;
    String description;
    String uri;
    boolean uploaded;
    int events;
    @ReadOnlyProperty
    List<String> materialRelations;

    public enum MaterialType {
        VIDEO,
        BOOK,
        DOCUMENT,
        TEXT,
        IMAGE,
        PDF,
        XLSX,
        PNG
    }

    public static Material create(String name, MaterialType materialType, String uri) {
        return new Material(
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                name,
                materialType,
                "",
                uri,
                false,
                0,
                List.of()
        );
    }
}
