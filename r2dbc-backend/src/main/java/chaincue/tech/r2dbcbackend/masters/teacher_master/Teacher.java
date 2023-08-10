package chaincue.tech.r2dbcbackend.masters.teacher_master;

import chaincue.tech.r2dbcbackend.masters.DomainObject;
import chaincue.tech.r2dbcbackend.masters.course_master.Course;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Table;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("teacher")
public class Teacher implements DomainObject {
    @Id
    String id;
    String name;
    String userId;
//    @Transient
//    List<Course> courses;

    public static Teacher createTeacher(String name) {
        return new Teacher(UUID.randomUUID().toString(), name, null);
    }

    public static Teacher fromRow(Map<String, Object> row) {
        if (row.get("t_id") != null) {
            return Teacher.builder()
                    .id((String) row.get("t_id"))
                    .name((String) row.get("t_name"))
                    .userId((String) row.get("t_user_id"))
                    .build();
        }
        else return null;
    }
}
