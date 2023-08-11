package chaincue.tech.r2dbcbackend2.masters.teacher_master;

import chaincue.tech.r2dbcbackend2.exeptions.TeacherNotFoundException;
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
public class TeacherHelper {
    private final CourseRepository courseRepository;
    private final TeacherRepository teacherRepository;
    private final TeacherCourseRelationRepository teacherCourseRelationRepository;
    private final UserRepository userRepository;

    @Transactional
    public Mono<Teacher> save(String userName, String firstName, String lastName) {
        User user = User.createUser(userName, firstName, lastName);
        Teacher teacher = Teacher.create(userName);
        teacher.setUserId(user.getId());
        user.setTeacherId(teacher.getId());
        return teacherRepository.save(teacher).then(userRepository.save(user)).thenReturn(teacher);
    }

    @Transactional
    public Mono<TeacherCourseRelation> addTeacherToCourse(String teacherId, String courseId) {
        var teacherCourseRelation = TeacherCourseRelation.create(teacherId, courseId);
        return Mono.zip(courseRepository.existsById(courseId), teacherRepository.existsById(teacherId),
                        (courseExists, teacherExists) -> {
                            if (!courseExists) throw new RuntimeException("Course not found");
                            if (!teacherExists) throw new RuntimeException("Teacher not found");
                            return teacherCourseRelation;
                        })
                .flatMap(teacherCourseRelationRepository::save);
    }

    public Mono<Teacher> getTeacherById(String teacherId) {
        return teacherRepository.findById(teacherId);
    }

    @Transactional
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

}
