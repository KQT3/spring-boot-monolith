package chaincue.tech.jpabackend.masters.course_master;

import chaincue.tech.jpabackend.masters.student_master.StudentRepository;
import chaincue.tech.jpabackend.masters.teacher_master.Teacher;
import chaincue.tech.jpabackend.masters.teacher_master.TeacherRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CourseHelper {
    private final CourseRepository courseRepository;
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;

    public Course saveCourse(String name) {
        Course course = Course.createCourse(name);
        return courseRepository.save(course);
    }

    @Transactional
    public Course addTeacherToCourse(String courseId, String teacherId) {
        Optional<Course> optionalCourse = courseRepository.findById(courseId);
        Optional<Teacher> optionalTeacher = teacherRepository.findById(teacherId);

        if(!optionalCourse.isPresent() || !optionalTeacher.isPresent()) {
            throw new RuntimeException("Course or Teacher not found.");
        }

        Course course = optionalCourse.get();
        Teacher teacher = optionalTeacher.get();

        course.getTeacherCourseRelations().add(teacher);
        teacher.getTeacherCourseRelations().add(course);

        courseRepository.save(course);
        teacherRepository.save(teacher);

        return course;
    }

    @Transactional
    public Course addStudentToCourse(String courseId, String teacherId) {
        var course = courseRepository.findById(courseId).orElseThrow(() -> new RuntimeException("Course or Teacher not found."));
        var student = studentRepository.findById(teacherId).orElseThrow(() -> new RuntimeException("Course or Teacher not found."));

        course.getStudentCourseRelations().add(student);
        student.getStudentCourseRelations().add(course);

        courseRepository.save(course);
        studentRepository.save(student);

        return course;
    }

    public List<Course> allCourses() {
        return courseRepository.findAll();
    }

    public Optional<Course> getCourseById(String courseId) {
        return courseRepository.findById(courseId);
    }
}
