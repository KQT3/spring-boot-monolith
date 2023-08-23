package chaincue.tech.r2dbcbackend2.masters.student_master;

import chaincue.tech.r2dbcbackend2.masters.course_master.Course;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface StudentContract {
    Mono<Student> save(String userName, String firstName, String lastName);

    Mono<StudentCourseRelation> addStudentToCourse(String studentId, String courseId);

    Mono<StudentCourseRelation> removeStudentFromCourse(String studentId, String courseId);

    Mono<Student> findStudentByIdWithCourses(String studentId);

    Flux<Student> findAll();

    Flux<Student> findAllWithRelations();

    Flux<Student> findAllWithRelations(Course course);

    Flux<Student> findByNameContainingIgnoreCase(String[] searchValues);
}
