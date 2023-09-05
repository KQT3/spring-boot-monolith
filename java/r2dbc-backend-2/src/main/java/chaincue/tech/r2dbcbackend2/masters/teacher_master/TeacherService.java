package chaincue.tech.r2dbcbackend2.masters.teacher_master;

import chaincue.tech.r2dbcbackend2.exeptions.RelationAlreadyExistsException;
import chaincue.tech.r2dbcbackend2.exeptions.TeacherNotFoundException;
import chaincue.tech.r2dbcbackend2.masters.course_master.Course;
import chaincue.tech.r2dbcbackend2.masters.course_master.repositories.CourseRepository;
import chaincue.tech.r2dbcbackend2.masters.teacher_master.repositories.TeacherCourseRelationRepository;
import chaincue.tech.r2dbcbackend2.masters.teacher_master.repositories.TeacherRepository;
import chaincue.tech.r2dbcbackend2.masters.user_master.User;
import chaincue.tech.r2dbcbackend2.masters.user_master.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

@Service
@AllArgsConstructor
public class TeacherService implements TeacherContract {
    private final CourseRepository courseRepository;
    private final TeacherRepository teacherRepository;
    private final TeacherCourseRelationRepository teacherCourseRelationRepository;
    private final UserRepository userRepository;

    @Transactional
    @Override
    public Mono<Teacher> save(String userName, String firstName, String lastName) {
        User user = User.createUser(userName, firstName, lastName);
        Teacher teacher = Teacher.create(userName);
        teacher.setUserId(user.getId());
        user.setTeacherId(teacher.getId());
        return teacherRepository.save(teacher).then(userRepository.save(user)).thenReturn(teacher);
    }

    @Transactional
    @Override
    public Mono<Teacher> getOrCreateTeacher(String userId) {
        return userRepository.findById(userId)
                .switchIfEmpty(Mono.defer(this::createUserWhenUserNotExist))
                .flatMap(user -> teacherRepository.findById(user.getTeacherId()));
    }

    private Mono<User> createUserWhenUserNotExist() {
        User user = User.createUser("", "", "");
        Teacher teacher = Teacher.create("");
        teacher.setUserId(user.getId());
        user.setTeacherId(teacher.getId());
        return teacherRepository.save(teacher).then(userRepository.save(user)).thenReturn(user);
    }

    @Transactional
    @Override
    public Mono<TeacherCourseRelation> addTeacherToCourse(String teacherId, String courseId) {
        return teacherCourseRelationRepository.existsByTeacherIdAndCourseId(teacherId, courseId)
                .flatMap(doesRelationExist -> {
                    if (doesRelationExist) {
                        String errorMessage = String.format("Relation already exists for Teacher ID %s and Course ID %s", teacherId, courseId);
                        return Mono.error(new RelationAlreadyExistsException(errorMessage));
                    }
                    return Mono.just(TeacherCourseRelation.create(teacherId, courseId));
                })
                .flatMap(teacherCourseRelationRepository::save);
    }

    public Mono<Teacher> getTeacherById(String teacherId) {
        return teacherRepository.findById(teacherId);
    }

    @Transactional
    @Override
    public Mono<Teacher> findTeacherByIdWithCourses(String teacherId) {
        return teacherRepository.findById(teacherId)
                .switchIfEmpty(Mono.error(new TeacherNotFoundException(teacherId)))
                .flatMap(this::fetchCoursesForTeacher);
    }

    private Mono<Teacher> fetchCoursesForTeacher(Teacher teacher) {
        return teacherCourseRelationRepository.findByTeacherId(teacher.getId())
                .map(TeacherCourseRelation::getCourseId)
                .collectList()
                .flatMap(this::findCoursesByIds)
                .doOnNext(teacher::setCourses)
                .thenReturn(teacher);
    }

    private Mono<List<Course>> findCoursesByIds(List<String> courseIds) {
        return courseRepository.findAllById(courseIds).collectList();
    }

    @Override
    public Flux<Teacher> findAll() {
        return teacherRepository.findAll();
    }

    public <P> Function<P, Mono<P>> updateParamWithTeachers(BiConsumer<P, List<Teacher>> setTeachers) {
        return param -> findAll()
                .collectList()
                .doOnNext(teachers -> setTeachers.accept(param, teachers))
                .thenReturn(param);
    }
}
