package chaincue.tech.jpabackend.masters.teacher_master;

import chaincue.tech.jpabackend.masters.course_master.Course;
import chaincue.tech.jpabackend.masters.user_master.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Entity(name = "teachers")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Teacher {
    @Id
    String id;
    String name;
    @OneToOne(mappedBy = "teacher", cascade = CascadeType.ALL)
    User user;
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "teacher_course_relations",
            joinColumns = @JoinColumn(name = "teacher_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id"))
    List<Course> teacherCourseRelations;

    public static Teacher createTeacher(String name) {
        return new Teacher(UUID.randomUUID().toString(), name, null, List.of());
    }

    @Override
    public String toString() {
        return "Teacher{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", user=" + user +
                ", teacherCourseRelations=" + teacherCourseRelations +
                '}';
    }
}
