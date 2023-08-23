package chaincue.tech.r2dbcbackend2.masters.student_master;

import chaincue.tech.r2dbcbackend2.masters.DomainObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.lang.NonNull;

import java.util.UUID;

@Data
@Table(name = "student_course_relations")
@AllArgsConstructor
public class StudentCourseRelation implements DomainObject {
    @NonNull
    @Id
    String id;
    @Column("student_id")
    private String studentId;
    @Column("course_id")
    private String courseId;
    StudentCourseRelationStatus studentCourseRelationStatus;
    int events;

    public static StudentCourseRelation create(String studentId, String courseId) {
        return new StudentCourseRelation(
                UUID.randomUUID().toString(),
                studentId,
                courseId,
                StudentCourseRelationStatus.ASSIGNED_TO,
                0
        );
    }

    public enum StudentCourseRelationStatus {
        ASSIGNED_TO,
        STARTED,
        COMPLETED
    }
}
