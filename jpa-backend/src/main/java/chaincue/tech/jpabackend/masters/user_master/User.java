package chaincue.tech.jpabackend.masters.user_master;

import chaincue.tech.jpabackend.masters.student_master.Student;
import chaincue.tech.jpabackend.masters.teacher_master.Teacher;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.UUID;

@Entity(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString(exclude = {"teacher", "student"})
public class User {
    @Id
    String id;
    String userName;
    @OneToOne(cascade = CascadeType.ALL)
    Teacher teacher;
    @OneToOne(cascade = CascadeType.ALL)
    Student student;

    public static User createUser(String username) {
        return new User(
                UUID.randomUUID().toString(),
                username,
                null,
                null
        );
    }
}
