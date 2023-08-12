package chaincue.tech.r2dbcbackend.masters.user_master;

import chaincue.tech.r2dbcbackend.masters.DomainObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Table(name = "users")
@AllArgsConstructor
@Data
public class User implements DomainObject {
    @Id
    String id;
    String userName;
    String teacherId;
    String studentId;

    public static User createUser(String username) {
        return new User(
                UUID.randomUUID().toString(),
                username,
                "null",
                "null"
        );
    }
}
