package chaincue.tech.r2dbcbackend2.masters.teacher_master;

import chaincue.tech.r2dbcbackend2.masters.DomainObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Data
@Table(name = "teacher_course_relations")
@AllArgsConstructor
public class TeacherCourseRelation implements DomainObject {
    @Id
    String id;
    String version;
    @Column("teacher_id")
    private String teacherId;
    @Column("course_id")
    private String courseId;
    TeacherCourseRelationStatus teacherCourseRelationStatus;
    int events;

    public static TeacherCourseRelation create(String teacherId, String courseId) {
        return new TeacherCourseRelation(
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                teacherId,
                courseId,
                TeacherCourseRelationStatus.MAIN,
                0);
    }

    public enum TeacherCourseRelationStatus {
        MAIN,
        ASSISTANT
    }
}
