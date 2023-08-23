package chaincue.tech.r2dbcbackend2.views.teacher_portal.dashboard;

import chaincue.tech.r2dbcbackend2.masters.course_master.Course;
import chaincue.tech.r2dbcbackend2.masters.course_master.CourseService;
import chaincue.tech.r2dbcbackend2.masters.student_master.Student;
import chaincue.tech.r2dbcbackend2.masters.student_master.StudentService;
import chaincue.tech.r2dbcbackend2.masters.teacher_master.Teacher;
import chaincue.tech.r2dbcbackend2.masters.teacher_master.TeacherService;
import chaincue.tech.r2dbcbackend2.masters.unit_master.Unit;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

import static chaincue.tech.r2dbcbackend2.utilities.JWTDecoderUtil.getSubIdFromToken;


@RestController
@RequestMapping("teacher-dashboard")
@RequiredArgsConstructor
@Slf4j
public class TeacherDashboardViewController {
    private final TeacherService teacherService;
    private final StudentService studentService;
    private final CourseService courseService;

    @GetMapping
    public Mono<TeacherDashboardViewDTO> dashboardView(@RequestHeader("Authorization") String token) {
        log.info("dashboardView");
        return teacherService.getOrCreateTeacher(getSubIdFromToken(token))
                .map(Param::new)
                .flatMap(param -> Mono.zip(
                                setStudents(param),
                                setCourses(param),
                                setTeachers(param)
                        )
                        .thenReturn(param))
                .map(TeacherDashboardViewController::toDTO);
    }

    private Mono<Param> setStudents(Param param) {
        return studentService.findAll().collectList().doOnNext(param::setStudents).thenReturn(param);
    }

    private Mono<Param> setCourses(Param param) {
        return courseService.findAllWithRelations().collectList().doOnNext(param::setCourses).thenReturn(param);
    }

    private Mono<Param> setTeachers(Param param) {
        return teacherService.findAll().collectList().doOnNext(param::setTeachers).thenReturn(param);
    }

    private static TeacherDashboardViewDTO toDTO(Param param) {
        return new TeacherDashboardViewDTO(
                param.getTeacher().getName(),
                param.getStudents().stream().map(TeacherDashboardViewController::toDTO).toArray(TeacherDashboardViewDTO.Student[]::new),
                param.getTeachers().stream().map(TeacherDashboardViewController::toDTO).toArray(TeacherDashboardViewDTO.Teacher[]::new),
                param.getCourses().stream().map(TeacherDashboardViewController::toDTO).toArray(TeacherDashboardViewDTO.Course[]::new));
    }

    public static TeacherDashboardViewDTO.Student toDTO(Student student) {
        return new TeacherDashboardViewDTO.Student(student.getId(), student.getName());
    }

    public static TeacherDashboardViewDTO.Teacher toDTO(Teacher teacher) {
        return new TeacherDashboardViewDTO.Teacher(teacher.getId(), teacher.getName());
    }

    public static TeacherDashboardViewDTO.Course toDTO(Course course) {
        return new TeacherDashboardViewDTO.Course(
                course.getId(),
                course.getName(),
                course.getDescription(),
                course.getEvents(),
                course.getStartDate(),
                course.getEndDate(),
                course.getStatus().toString(),
                course.getUnits().stream().map(TeacherDashboardViewController::toDTO).toArray(TeacherDashboardViewDTO.Unit[]::new));
    }

    public static TeacherDashboardViewDTO.Unit toDTO(Unit unit) {
        return new TeacherDashboardViewDTO.Unit(unit.getId(), unit.getName());
    }

    @Data
    private static class Param {
        Teacher teacher;
        List<Student> students = new ArrayList<>();
        List<Teacher> teachers = new ArrayList<>();
        List<Course> courses = new ArrayList<>();

        public Param(Teacher teacher) {
            this.teacher = teacher;
        }
    }
}
