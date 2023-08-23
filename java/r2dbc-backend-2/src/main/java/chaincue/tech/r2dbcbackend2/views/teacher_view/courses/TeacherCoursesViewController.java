package chaincue.tech.r2dbcbackend2.views.teacher_view.courses;

import chaincue.tech.r2dbcbackend2.masters.course_master.Course;
import chaincue.tech.r2dbcbackend2.masters.course_master.CourseService;
import chaincue.tech.r2dbcbackend2.masters.teacher_master.Teacher;
import chaincue.tech.r2dbcbackend2.masters.teacher_master.TeacherService;
import chaincue.tech.r2dbcbackend2.masters.unit_master.Unit;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import static chaincue.tech.r2dbcbackend2.utilities.JWTDecoderUtil.getSubIdFromToken;


@RestController
@RequestMapping("teacher-courses")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Slf4j
public class TeacherCoursesViewController {
    private final TeacherService teacherService;
    private final CourseService courseService;

    @GetMapping
    public Mono<TeacherCoursesViewDTO> coursesView(@RequestHeader("Authorization") String token) {
        return teacherService.getOrCreateTeacher(getSubIdFromToken(token))
                .map(Param::new)
                .flatMap(param -> Mono.zip(
                                setMyCourses(param),
                                setAllCourses(param)
                        )
                        .thenReturn(param))
                .map(TeacherCoursesViewController::toDTO);
    }

    @PostMapping("create-course/{courseName}")
    public Mono<String> createCourse(@RequestHeader("Authorization") String token, @PathVariable String courseName) {
        log.info("courseName: {}", courseName);
        return teacherService.getOrCreateTeacher(getSubIdFromToken(token))
                .map(Param::new)
                .flatMap(param -> courseService.save(courseName).mapNotNull(Course::getId));
    }

    @PostMapping("search")
    public Flux<TeacherCoursesViewDTO.Course> searchCoursesView(@RequestHeader("Authorization") String token, @RequestBody TeacherFilterCourseDTO searchValues) {
        log.info("searchValues: {}", searchValues);
        return  Flux.from(teacherService.getOrCreateTeacher(getSubIdFromToken(token))
                .map(Param::new)
                .flatMap(param -> filterCoursesBySearchValues(searchValues, param))
                .map(param -> Flux.fromIterable(param.getAllCourses())
                        .sort(Comparator.comparing(Course::getName))
                        .map(TeacherCoursesViewController::toDTO)))
                .flatMap(courseFlux -> courseFlux);
    }

    private Mono<Param> filterCoursesBySearchValues(TeacherFilterCourseDTO searchValues, Param param) {
        return courseService.findAllWithRelations()
                .filter(course -> {
                    if (searchValues.searchValues().length > 0)
                        return Arrays.stream(searchValues.searchValues())
                                .anyMatch(s -> course.getName().toLowerCase().contains(s.toLowerCase()));
                    return true;
                })
                .collectList()
                .doOnNext(param::setAllCourses)
                .thenReturn(param);
    }

    private Mono<Param> setAllCourses(Param param) {
        return courseService.findAllWithRelations()
                .collectList()
                .doOnNext(param::setAllCourses)
                .thenReturn(param);
    }

    private Mono<Param> setMyCourses(Param param) {
        return courseService.findAllWithRelations()
                .filter(course -> course.getTeachers().stream()
                        .allMatch(teacher -> Objects.equals(teacher.getId(), param.getTeacher().getId())))
                .collectList()
                .doOnNext(param::setMyCourses)
                .thenReturn(param);
    }

    private static TeacherCoursesViewDTO toDTO(Param param) {
        return new TeacherCoursesViewDTO(
                param.getTeacher().getId(),
                param.getTeacher().getName(),
                param.getMyCourses().stream().map(TeacherCoursesViewController::toDTO).toArray(TeacherCoursesViewDTO.Course[]::new),
                param.getAllCourses().stream().map(TeacherCoursesViewController::toDTO).toArray(TeacherCoursesViewDTO.Course[]::new)
        );
    }

    public static TeacherCoursesViewDTO.Course toDTO(Course course) {
        return new TeacherCoursesViewDTO.Course(
                course.getId(),
                course.getName(),
                course.getStartDate().toString(),
                course.getEndDate().toString(),
                course.getStatus().toString(),
                course.getUnits().stream().map(TeacherCoursesViewController::toDTO).toArray(TeacherCoursesViewDTO.Unit[]::new)
        );
    }

    private static TeacherCoursesViewDTO.Unit toDTO(Unit unit) {
        return new TeacherCoursesViewDTO.Unit(unit.getId(), unit.getName());
    }

    @Data
    private static class Param {
        private Teacher teacher;
        private List<Course> myCourses;
        private List<Course> allCourses;

        public Param(Teacher teacher) {
            this.teacher = teacher;
        }
    }
}
