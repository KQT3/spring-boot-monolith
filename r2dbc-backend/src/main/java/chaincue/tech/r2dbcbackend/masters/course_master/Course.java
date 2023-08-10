package chaincue.tech.r2dbcbackend.masters.course_master;

import chaincue.tech.r2dbcbackend.masters.DomainObject;
import chaincue.tech.r2dbcbackend.masters.teacher_master.Teacher;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Table;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table("course")
public class Course implements DomainObject {
    @Id
    String id;
    String name;
    List<Teacher> teachers;
    //    List<String> studentCourseRelations;
//    List<String> teacherCourseRelations;

    public enum Status {
        WIP,
        ACTIVE,
        COMPLETE,
        INACTIVE
    }

    public static Mono<Course> fromRows(List<Map<String, Object>> rows) {
        return Mono.just(Course.builder()
                .id(rows.get(0).get("c_id").toString())
                .name("c_name")
                .teachers(rows.stream()
                        .map(Teacher::fromRow)
                        .filter(Objects::nonNull)
                        .toList())
                .build());
    }

}


