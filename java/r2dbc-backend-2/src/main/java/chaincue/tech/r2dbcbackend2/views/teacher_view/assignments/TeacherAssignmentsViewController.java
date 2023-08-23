package chaincue.tech.r2dbcbackend2.views.teacher_view.assignments;

import chaincue.tech.r2dbcbackend2.masters.assignment_master.Assignment;
import chaincue.tech.r2dbcbackend2.masters.assignment_master.AssignmentService;
import chaincue.tech.r2dbcbackend2.masters.course_master.Course;
import chaincue.tech.r2dbcbackend2.masters.course_master.CourseService;
import chaincue.tech.r2dbcbackend2.masters.teacher_master.Teacher;
import chaincue.tech.r2dbcbackend2.masters.teacher_master.TeacherService;
import chaincue.tech.r2dbcbackend2.masters.unit_master.Unit;
import chaincue.tech.r2dbcbackend2.masters.unit_master.UnitService;
import chaincue.tech.r2dbcbackend2.views.teacher_view.assignments.DTOs.TeacherCreateAssignmentReqBody;
import chaincue.tech.r2dbcbackend2.views.teacher_view.assignments.DTOs.TeacherDuplicateAssignmentsRequestBody;
import chaincue.tech.r2dbcbackend2.views.teacher_view.assignments.DTOs.TeacherFilterAssignmentDTO;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static chaincue.tech.r2dbcbackend2.utilities.JWTDecoderUtil.getSubIdFromToken;


@RestController
@RequestMapping("teacher-assignments")
@RequiredArgsConstructor
@Slf4j
public class TeacherAssignmentsViewController {
    private final TeacherService teacherService;
    private final CourseService courseService;
    private final UnitService unitService;
    private final AssignmentService assignmentService;

    @GetMapping
    public Mono<TeacherAssignmentsViewDTO> assignmentsView(@RequestHeader("Authorization") String token) {
        return teacherService.getOrCreateTeacher(getSubIdFromToken(token))
                .map(Param::new)
                .flatMap(param -> Mono.zip(
                                setCourses(param),
                                setUnits(param),
                                setAssignments(param)
                        )
                        .thenReturn(param))
                .map(TeacherAssignmentsViewController::toDTO);
    }

    @PostMapping
    public Mono<String> createAssignment(@RequestHeader("Authorization") String token,
                                         @RequestBody TeacherCreateAssignmentReqBody teacherCreateAssignmentReqBody) {
        log.info("courseName: {}", teacherCreateAssignmentReqBody.name());
        return teacherService.getOrCreateTeacher(getSubIdFromToken(token))
                .map(Param::new)
                .flatMap(param -> assignmentService.save(teacherCreateAssignmentReqBody.name(), null).mapNotNull(Assignment::getId));
    }

    //TODO duplicateAssignment
    @PostMapping("duplicate")
    public Flux<String> duplicateAssignment(@RequestHeader("Authorization") String token,
                                            @RequestBody TeacherDuplicateAssignmentsRequestBody teacherDuplicateAssignmentsRequestBody) {
        return Flux.empty();
    }

    @PostMapping("search")
    public Flux<TeacherAssignmentsViewDTO.Assignment> searchAssignmentsView(@RequestHeader("Authorization") String token,
                                                                            @RequestBody TeacherFilterAssignmentDTO searchValues) {
        log.info("searchValues: {}", searchValues);
        return Flux.from(teacherService.getOrCreateTeacher(getSubIdFromToken(token))
                        .map(Param::new)
                        .flatMap(param -> filterAssignmentBySearchValues(searchValues, param))
                        .map(param -> Flux.fromIterable(param.getAssignments())
                                .sort(Comparator.comparing(Assignment::getName))
                                .map(TeacherAssignmentsViewController::toDTO)))
                .flatMap(courseFlux -> courseFlux);
    }

    private Mono<Param> filterAssignmentBySearchValues(TeacherFilterAssignmentDTO searchValues, Param param) {
        return assignmentService.findAllWithRelations()
                .filter(assignment -> {
                    if (searchValues.searchValues().length > 0)
                        return Arrays.stream(searchValues.searchValues())
                                .anyMatch(s -> assignment.getName().toLowerCase().contains(s.toLowerCase()));
                    return true;
                })
                .collectList()
                .doOnNext(param::setAssignments)
                .thenReturn(param);
    }

    private Mono<Param> setCourses(Param param) {
        return courseService.findAllWithRelations().collectList().doOnNext(param::setCourses).thenReturn(param);
    }

    private Mono<Param> setUnits(Param param) {
        return unitService.findAllWithRelations().collectList().doOnNext(param::setUnits).thenReturn(param);
    }

    private Mono<Param> setAssignments(Param param) {
        return assignmentService.findAllWithRelations().collectList().doOnNext(param::setAssignments).thenReturn(param);
    }

    private static TeacherAssignmentsViewDTO toDTO(Param param) {
        return new TeacherAssignmentsViewDTO(
                param.getTeacher().getId(),
                param.getTeacher().getName(),
                param.getCourses().stream().map(TeacherAssignmentsViewController::toDTO).toArray(TeacherAssignmentsViewDTO.Course[]::new),
                param.getAssignmentTypes().toArray(String[]::new),
                param.getAssignments().stream()
                        .sorted(Comparator.comparing(Assignment::getCreatedAt).reversed())
                        .map(TeacherAssignmentsViewController::toDTO)
                        .toArray(TeacherAssignmentsViewDTO.Assignment[]::new));
    }

    private static TeacherAssignmentsViewDTO.Course toDTO(Course course) {
        return new TeacherAssignmentsViewDTO.Course(
                course.getId(),
                course.getName(),
                course.getUnits().stream().map(TeacherAssignmentsViewController::toDTO).toArray(TeacherAssignmentsViewDTO.Unit[]::new));
    }

    private static TeacherAssignmentsViewDTO.Unit toDTO(Unit unit) {
        return new TeacherAssignmentsViewDTO.Unit(
                unit.getId(),
                unit.getName());
    }

    private static TeacherAssignmentsViewDTO.Assignment toDTO(Assignment assignment) {
        return new TeacherAssignmentsViewDTO.Assignment(
                assignment.getId(),
                assignment.getName(),
                "assignment.courseName()",
                assignment.getUnitId(),
                assignment.getAssignmentType()
        );
    }

    @Data
    private static class Param {
        private Teacher teacher;
        private List<Course> courses = new ArrayList<>();
        private List<Unit> units = new ArrayList<>();
        private List<String> assignmentTypes = new ArrayList<>();
        private List<Assignment> assignments = new ArrayList<>();

        public Param(Teacher teacher) {
            this.teacher = teacher;
            assignmentTypes.add("QueriesAssignmentType.QUERIES_ASSIGNMENT_TYPE");
            assignmentTypes.add("GitLabAssignmentType.GIT_LAB_ASSIGNMENT_TYPE");
        }
    }
}
