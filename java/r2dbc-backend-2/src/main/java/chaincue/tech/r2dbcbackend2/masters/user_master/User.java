package chaincue.tech.r2dbcbackend2.masters.user_master;

import chaincue.tech.r2dbcbackend2.masters.DomainObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.lang.NonNull;

import java.util.UUID;

@Table(name = "users")
@AllArgsConstructor
@Data
public class User implements DomainObject {
    @Id
    @NonNull
    String id;
    String version;
    String userName;
    String password;
    @Column("teacher_id")
    String teacherId;
    @Column("student_id")
    String studentId;
    int events;
    String firstName;
    String lastName;
    String email;
    String gender;
    String phoneNumber;

    public static User createUser(String username, String firstname, String lastname) {
        return new User(
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                username,
                "null",
                null,
                null,
                0,
                firstname,
                lastname,
                username,
                "null",
                "null"
        );
    }
}
