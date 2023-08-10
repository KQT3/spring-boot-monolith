package chaincue.tech.r2dbcbackend.masters.student_master;

import chaincue.tech.r2dbcbackend.masters.DomainObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Table("student")
@AllArgsConstructor
@Data
public class Student implements DomainObject {
    @Id
    String id;
    String name;
    @Column("user_id")
    String userId;
//    List<String> studentCourseRelations;

    public static Student createStudent(String userId){
        return new Student(
                UUID.randomUUID().toString(),
                "name",
                userId
        );
    }
}
