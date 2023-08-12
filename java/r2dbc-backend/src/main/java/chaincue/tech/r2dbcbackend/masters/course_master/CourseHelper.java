package chaincue.tech.r2dbcbackend.masters.course_master;

import chaincue.tech.r2dbcbackend.exeptions.CourseAlreadyExistsException;
import chaincue.tech.r2dbcbackend.exeptions.CourseNotFoundException;
import chaincue.tech.r2dbcbackend.masters.teacher_master.TeacherRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class CourseHelper {
    private final CourseRepository courseRepository;

    @Transactional
    public Mono<Course> saveCourse(String name) {
        Course course = Course.builder()
                .id(UUID.randomUUID().toString())
                .name(name)
                .build();
        return courseRepository.save(course);
    }

    @Transactional
    public Mono<Course> findById(String courseId) {
        return courseRepository.findById(courseId)
                .switchIfEmpty(Mono.error(new CourseNotFoundException(courseId)));
    }

    public Mono<Course> createCourse(String name) {
        return this.courseRepository.findByName(name)
                .flatMap(course -> Mono.error(new CourseAlreadyExistsException(course.getName())))
                .defaultIfEmpty(Course.builder().name(name).teachers(List.of()).build()).cast(Course.class)
                .flatMap(courseRepository::save);
    }

//    @Transactional
//    public Mono<Course> addTeacherToCourse(String courseId, String teacherId) {
//        return courseRepository.findById(courseId)
//                .flatMap(course -> teacherRepository.findById(teacherId)
//                        .flatMap(teacher -> {
//                            course.getTeachers().add(teacher);
//                            return courseRepository.save(course);
//                        }));
//    }
}
