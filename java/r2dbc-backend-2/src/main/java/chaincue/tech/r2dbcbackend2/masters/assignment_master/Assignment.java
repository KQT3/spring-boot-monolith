package chaincue.tech.r2dbcbackend2.masters.assignment_master;

import chaincue.tech.r2dbcbackend2.masters.DomainObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.lang.NonNull;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Table("assignments")
@AllArgsConstructor
@Data
public class Assignment implements DomainObject {
    @Id
    @NonNull
    String id;
    String version;
    String name;
    String assignmentType;
    @Column("unit_id")
    String unitId;
    String assignmentTypeId;
    int events;
    LocalDate createdAt;
    @ReadOnlyProperty
    List<String> materialRelations;

    public static Assignment create(String name, String unitId) {
        return new Assignment(
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                name,
                null,
                unitId,
                null,
                0,
                LocalDate.now(),
                List.of()
        );
    }
}
