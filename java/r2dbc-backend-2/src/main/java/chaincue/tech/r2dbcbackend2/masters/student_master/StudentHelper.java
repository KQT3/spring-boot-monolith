package chaincue.tech.r2dbcbackend2.masters.student_master;

import chaincue.tech.r2dbcbackend2.exeptions.StudentNotFoundException;
import chaincue.tech.r2dbcbackend2.masters.course_master.Course;
import chaincue.tech.r2dbcbackend2.masters.course_master.CourseRepository;
import chaincue.tech.r2dbcbackend2.masters.user_master.User;
import chaincue.tech.r2dbcbackend2.masters.user_master.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@AllArgsConstructor
public class StudentHelper {
    private final StudentRepository studentRepository;
    private final UserRepository userRepository;
    private final StudentCourseRelationRepository studentCourseRelationRepository;
    private final CourseRepository courseRepository;

    @Transactional
    public Mono<Student> save(String userName, String firstName, String lastName) {
        User user = User.createUser(userName, firstName, lastName);
        var student = Student.create(userName);
        student.setUserId(user.getId());
        user.setStudentId(student.getId());
        return studentRepository.save(student).then(userRepository.save(user)).thenReturn(student);
    }

    @Transactional
    public Mono<StudentCourseRelation> addStudentToCourse(String studentId, String courseId) {
        var studentCourseRelation = StudentCourseRelation.create(studentId, courseId);
        return Mono.zip(courseRepository.existsById(courseId), studentRepository.existsById(studentId),
                        (courseExists, studentExists) -> {
                            if (!courseExists) throw new RuntimeException("Course not found");
                            if (!studentExists) throw new RuntimeException("Student not found");
                            return studentCourseRelation;
                        })
                .flatMap(studentCourseRelationRepository::save);
    }

    @Transactional
    public Mono<Student> findStudentByIdWithCourses(String studentId) {
        return studentRepository.findById(studentId)
                .switchIfEmpty(Mono.error(new StudentNotFoundException(studentId)))
                .flatMap(this::fetchCoursesForStudent);
    }

    private Mono<Student> fetchCoursesForStudent(Student student) {
        return studentCourseRelationRepository.findByStudentId(student.getId())
                .map(StudentCourseRelation::getCourseId)
                .collectList()
                .flatMap(this::findCoursesByIds)
                .doOnNext(student::setCourses)
                .thenReturn(student);
    }

    private Mono<List<Course>> findCoursesByIds(List<String> courseIds) {
        return courseRepository.findAllById(courseIds).collectList();
    }
}
