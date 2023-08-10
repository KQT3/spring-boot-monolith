package chaincue.tech.r2dbcbackend.masters.course_master;

import chaincue.tech.r2dbcbackend.masters.teacher_master.Teacher;
import chaincue.tech.r2dbcbackend.masters.teacher_master.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CourseRepositoryImpl implements CourseRepository {
    private final TeacherRepository teacherRepository;
    private final DatabaseClient client;
    private static final String SELECT_QUERY = """
            SELECT c.id c_id, c.name c_name, t.id t_id, t.name t_name, t.user_id t_user_id
            FROM course c
            LEFT JOIN teacher_course tc ON tc.course_id = c.id
            LEFT JOIN teacher t ON t.id = tc.teacher_id
            """;

    @Override
    public Flux<Course> findAll() {
        String query = String.format("%s ORDER BY c.id", SELECT_QUERY);
        return client.sql(query)
                .fetch()
                .all()
                .bufferUntilChanged(result -> result.get("c_id"))
                .flatMap(Course::fromRows);
    }

    @Override
    public Mono<Course> findById(String id) {
        String query = String.format("%s WHERE c.id = :id", SELECT_QUERY);

        return client.sql(query)
                .bind("id", id)
                .fetch()
                .all()
                .bufferUntilChanged(result -> result.get("c_id"))
                .flatMap(Course::fromRows)
                .singleOrEmpty();
    }

    @Override
    public Mono<Course> findByName(String name) {
        String query = String.format("%s WHERE c.name = :name", SELECT_QUERY);
        return client.sql(query)
                .bind("name", name)
                .fetch()
                .all()
                .bufferUntilChanged(result -> result.get("c_id"))
                .flatMap(Course::fromRows)
                .singleOrEmpty();
    }

    @Override
    @Transactional
    public Mono<Course> save(Course course) {
        return saveCourse(course)
                .flatMap(this::saveTeachers)
//                .flatMap(this::deleteTeacherCourse)
                .flatMap(this::saveTeacherCourse);
    }

    @Override
    @Transactional
    public Mono<Void> delete(Course course) {
        return this.deleteTeacherCourse(course)
                .flatMap(this::deleteCourse)
                .then();
    }

    /*TODO  course.setId(UUID.randomUUID().toString()); dont work dosn't add */
    private Mono<Course> saveCourse(Course course) {
        if (course.getId() == null) {
            return client.sql("INSERT INTO course(name) VALUES(:name)")
                    .bind("name", course.getName())
                    .bind("id", UUID.randomUUID().toString())
                    .fetch()
                    .first()
                    .thenReturn(course);
        } else {
            return this.client.sql("UPDATE course SET name = :name WHERE id = :id")
                    .bind("name", course.getName())
                    .bind("id", course.getId())
                    .fetch().first()
                    .thenReturn(course);
        }
    }

    private Mono<Course> saveTeachers(Course course) {
        return Flux.fromIterable(course.getTeachers())
                .flatMap(teacherRepository::save)
                .collectList()
                .doOnNext(course::setTeachers)
                .thenReturn(course);
    }

    private Mono<Course> saveTeacherCourse(Course course) {
        String query = "INSERT INTO teacher_course(course_id, teacher_id) VALUES (:courseId, :teacherId)";

        return Flux.fromIterable(course.getTeachers())
                .flatMap(teacher -> client.sql(query)
                        .bind("courseId", course.getId())
                        .bind("teacherId", teacher.getId())
                        .fetch().rowsUpdated())
                .collectList()
                .thenReturn(course);
    }

    private Mono<Course> deleteTeacherCourse(Course course) {
        String query = "DELETE FROM teacher_course WHERE course_id = :id OR teacher_id in (:ids)";
        List<String> teacherIds = course.getTeachers().stream().map(Teacher::getId).toList();

        return Mono.just(course)
                .flatMap(dep -> client.sql(query)
                        .bind("id", course.getId())
                        .bind("ids", teacherIds.isEmpty() ? List.of(0) : teacherIds)
                        .fetch().rowsUpdated())
                .thenReturn(course);
    }

    private Mono<Void> deleteCourse(Course course) {
        return client.sql("DELETE FROM course WHERE id = :id")
                .bind("id", course.getId())
                .fetch().first()
                .then();
    }
}
