package chaincue.tech.jpabackend.masters.student_master;

import chaincue.tech.jpabackend.masters.course_master.Course;
import chaincue.tech.jpabackend.masters.user_master.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;
import java.util.UUID;

@Entity(name = "students")
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class Student {
    @Id
    String id;
    String name;
    @OneToOne(mappedBy = "student", cascade = CascadeType.ALL)
    User user;
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "student_course_relations",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id"))
    List<Course> studentCourseRelations;

    public static Student createStudent(String name) {
        return new Student(
                UUID.randomUUID().toString(),
                name,
                null,
                List.of()
        );
    }
}
