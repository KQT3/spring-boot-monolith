package chaincue.tech.r2dbcbackend.masters.course_master;

import chaincue.tech.r2dbcbackend.masters.teacher_master.TeacherRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class CourseHelper {
    private final CourseRepository courseRepository;
    private final TeacherRepository teacherRepository;

    @Transactional
    public Mono<Course> saveCourse(String name) {
        Course course = Course.createCourse(name);
        return courseRepository.save(course);
    }

    @Transactional
    public Mono<Course> addTeacherToCourse(String courseId, String teacherId) {
        return courseRepository.findById(courseId)
                .flatMap(course -> teacherRepository.findById(teacherId)
                        .flatMap(teacher -> {
                            course.getTeachers().add(teacher);
                            return courseRepository.save(course);
                        }));
    }
}
