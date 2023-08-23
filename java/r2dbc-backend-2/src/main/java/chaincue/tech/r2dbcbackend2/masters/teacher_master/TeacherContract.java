package chaincue.tech.r2dbcbackend2.masters.teacher_master;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TeacherContract {
    Mono<Teacher> save(String userName, String firstName, String lastName);

    Mono<TeacherCourseRelation> addTeacherToCourse(String teacherId, String courseId);

    Mono<Teacher> getTeacherById(String teacherId);

    Mono<Teacher> findTeacherByIdWithCourses(String teacherId);

    Mono<Teacher> getOrCreateTeacher(String userId);

    Flux<Teacher> findAll();
}
