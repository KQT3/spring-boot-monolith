package chaincue.tech.jpabackend.masters.course_master;

import chaincue.tech.jpabackend.masters.student_master.Student;
import chaincue.tech.jpabackend.masters.teacher_master.Teacher;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;
import java.util.UUID;

@Entity(name = "courses")
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString(exclude = {"teacherCourseRelations", "studentCourseRelations"})
public class Course {
    @Id
    String id;
    String name;
    @ManyToMany(mappedBy = "teacherCourseRelations")
    List<Teacher> teacherCourseRelations;
    @ManyToMany(mappedBy = "studentCourseRelations")
    List<Student> studentCourseRelations;

    public static Course createCourse(String name) {
        return new Course(
                UUID.randomUUID().toString(),
                name,
                List.of(),
                List.of()
        );
    }
}
