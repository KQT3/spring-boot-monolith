package chaincue.tech.r2dbcbackend2.masters.student_master;

import chaincue.tech.r2dbcbackend2.exeptions.RelationAlreadyExistsException;
import chaincue.tech.r2dbcbackend2.exeptions.RelationNotFoundException;
import chaincue.tech.r2dbcbackend2.exeptions.StudentNotFoundException;
import chaincue.tech.r2dbcbackend2.masters.course_master.Course;
import chaincue.tech.r2dbcbackend2.masters.course_master.repositories.CourseRepository;
import chaincue.tech.r2dbcbackend2.masters.student_master.repositories.StudentCourseRelationRepository;
import chaincue.tech.r2dbcbackend2.masters.student_master.repositories.StudentRepository;
import chaincue.tech.r2dbcbackend2.masters.user_master.User;
import chaincue.tech.r2dbcbackend2.masters.user_master.UserRepository;
import chaincue.tech.r2dbcbackend2.views.teacher_view.dashboard.TeacherDashboardViewController;
import chaincue.tech.r2dbcbackend2.views.teacher_view.dashboard.TeacherDashboardViewDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

@Service
@AllArgsConstructor
public class StudentService implements StudentContract {
    private final StudentRepository studentRepository;
    private final UserRepository userRepository;
    private final StudentCourseRelationRepository studentCourseRelationRepository;
    private final CourseRepository courseRepository;

    @Transactional
    @Override
    public Mono<Student> save(String userName, String firstName, String lastName) {
        User user = User.createUser(userName, firstName, lastName);
        var student = Student.create(userName);
        student.setUserId(user.getId());
        user.setStudentId(student.getId());
        return studentRepository.save(student).then(userRepository.save(user)).thenReturn(student);
    }

    @Transactional
    @Override
    public Mono<StudentCourseRelation> addStudentToCourse(String studentId, String courseId) {
        return studentCourseRelationRepository.existsByStudentIdAndCourseId(studentId, courseId)
                .flatMap(doesRelationExist -> {
                    if (doesRelationExist) {
                        String errorMessage = String.format("Relation already exists for Student ID %s and Course ID %s", studentId, courseId);
                        return Mono.error(new RelationAlreadyExistsException(errorMessage));
                    }
                    return Mono.just(StudentCourseRelation.create(studentId, courseId));
                })
                .flatMap(studentCourseRelationRepository::save);
    }

    @Override
    public Mono<StudentCourseRelation> removeStudentFromCourse(String studentId, String courseId) {
        return studentCourseRelationRepository.findByStudentIdAndCourseId(studentId, courseId)
                .switchIfEmpty(Mono.error(new RelationNotFoundException(studentId + " | " + courseId)))
                .flatMap(relation -> studentCourseRelationRepository.deleteById(relation.getId())
                        .thenReturn(relation));
    }

    @Transactional
    @Override
    public Mono<Student> findStudentByIdWithCourses(String studentId) {
        return studentRepository.findById(studentId)
                .switchIfEmpty(Mono.error(new StudentNotFoundException(studentId)))
                .flatMap(this::fetchCoursesForStudent);
    }

    @Transactional
    @Override
    public Flux<Student> findAll() {
        return studentRepository.findAll();
    }

    @Override
    public Flux<Student> findAllWithRelations() {
        return studentRepository.findAll().flatMap(this::fetchCoursesForStudent);
    }

    @Override
    public Flux<Student> findAllWithRelations(Course course) {
        return studentCourseRelationRepository.findByCourseId(course.getId())
                .flatMap(relation -> studentRepository.findById(relation.getStudentId())
                        .flatMap(this::fetchCoursesForStudent));
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

    @Override
    public Flux<Student> findByNameContainingIgnoreCase(String[] searchValues) {
        return studentRepository.findAll()
                .filter(student -> {
                    if (searchValues == null || searchValues.length == 0) return true;
                    return Arrays.stream(searchValues).anyMatch(val -> student.getName().toLowerCase().contains(val.toLowerCase()));
                })
                .flatMap(this::fetchCoursesForStudent);
    }

    public <P> Function<P, Mono<P>> updateParamWithStudents(BiConsumer<P, List<Student>> setStudents) {
        return param -> findAllWithRelations()
                .collectList()
                .doOnNext(students -> setStudents.accept(param, students))
                .thenReturn(param);
    }
}
