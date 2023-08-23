package chaincue.tech.r2dbcbackend2.views.teacher_portal.course_edit;

import chaincue.tech.r2dbcbackend2.masters.course_master.Course;
import chaincue.tech.r2dbcbackend2.masters.course_master.CourseService;
import chaincue.tech.r2dbcbackend2.masters.material_master.Material;
import chaincue.tech.r2dbcbackend2.masters.material_master.MaterialService;
import chaincue.tech.r2dbcbackend2.masters.student_master.Student;
import chaincue.tech.r2dbcbackend2.masters.student_master.StudentService;
import chaincue.tech.r2dbcbackend2.masters.tag_master.Tag;
import chaincue.tech.r2dbcbackend2.masters.tag_master.TagService;
import chaincue.tech.r2dbcbackend2.masters.teacher_master.Teacher;
import chaincue.tech.r2dbcbackend2.masters.teacher_master.TeacherService;
import chaincue.tech.r2dbcbackend2.masters.unit_master.Unit;
import chaincue.tech.r2dbcbackend2.masters.unit_master.UnitService;
import chaincue.tech.r2dbcbackend2.views.teacher_portal.course_edit.DTOs.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static chaincue.tech.r2dbcbackend2.utilities.JWTDecoderUtil.getSubIdFromToken;


@RestController
@RequestMapping("teacher-course-edit")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Slf4j
public class TeacherCourseEditViewController {
    private final TeacherService teacherService;
    private final CourseService courseService;
    private final UnitService unitService;
    private final MaterialService materialService;
    private final TagService tagService;
    private final StudentService studentService;

    @GetMapping("{courseId}")
    public Mono<TeacherCourseEditDTO> courseEditView(@RequestHeader("Authorization") String token, @PathVariable String courseId) {
        log.info("course id {}", courseId);
        return toTeacherCourseEditDTO(token, courseId, Optional.empty());
    }

    @PostMapping("search-unit/{courseId}")
    public Flux<TeacherCourseEditDTO.Unit> searchUnit(@RequestHeader("Authorization") String token, @PathVariable String courseId, @RequestBody TeacherCourseEditSearchValues searchValues) {
        log.info("course id {}", courseId);
        return Flux.from(teacherService.getOrCreateTeacher(getSubIdFromToken(token))
                .map(Param::new)
                .flatMap(param -> setCourse(param, courseId))
                .flatMap(param -> unitService.findByNameContainingIgnoreCase(searchValues.searchValues())
                        .collectList()
                        .doOnNext(param::setAllUnits)
                        .thenReturn(param))
                .flatMapMany(param -> Flux.fromIterable(param.getAllUnits())
                        .map(unit -> TeacherCourseEditViewController.toDTO(unit, Optional.of(param::isUnitAttachedToCourse)))
                        .sort(Comparator.comparing(TeacherCourseEditDTO.Unit::attachedToCourse).reversed()))
        );
    }

    @PostMapping("search-material/{courseId}")
    public Flux<TeacherCourseEditDTO.Material> searchMaterial(@RequestHeader("Authorization") String token, @PathVariable String courseId, @RequestBody TeacherCourseEditSearchValues searchValues) {
        log.info("course id {}", courseId);
        return Flux.from(teacherService.getOrCreateTeacher(getSubIdFromToken(token))
                .map(Param::new)
                .flatMap(param -> setCourse(param, courseId))
                .flatMap(param -> materialService.findByNameContainingIgnoreCase(searchValues.searchValues())
                        .collectList()
                        .doOnNext(param::setAllMaterials)
                        .thenReturn(param))
                .flatMapMany(param -> Flux.fromIterable(param.getAllMaterials())
                        .map(material -> TeacherCourseEditViewController.toDTO(material, Optional.of(param::isMaterialAttachedToCourse), Optional.of(param::getCourseByMaterialRelationId), Optional.empty())))
                .sort(Comparator.comparing(TeacherCourseEditDTO.Material::attachedToCourse).reversed()));
    }

    @PostMapping("search-student/{courseId}")
    public Flux<TeacherCourseEditDTO.Student> searchStudent(@RequestHeader("Authorization") String token, @PathVariable String courseId, @RequestBody TeacherCourseEditSearchValues searchValues) {
        log.info("course id {}", courseId);
        return Flux.from(teacherService.getOrCreateTeacher(getSubIdFromToken(token))
                .map(Param::new)
                .flatMap(param -> setCourse(param, courseId))
                .flatMap(param -> studentService.findByNameContainingIgnoreCase(searchValues.searchValues())
                        .collectList()
                        .doOnNext(param::setAllStudents)
                        .thenReturn(param))
                .flatMapMany(param -> Flux.fromIterable(param.getAllStudents())
                        .map(student -> TeacherCourseEditViewController.toDTO(student, Optional.of(param::isStudentAttachedToCourse)))
                        .sort(Comparator.comparing(TeacherCourseEditDTO.Student::attachedToCourse).reversed())));
    }

    @PutMapping("update-course-field")
    public Mono<TeacherCourseEditDTO> updateCourseField(@RequestHeader("Authorization") String token, @RequestBody TeacherUpdateCourseRequestBody requestBody) {
        return toTeacherCourseEditDTO(token, requestBody.courseId(), Optional.of(param -> courseService.updateCourseField(requestBody.courseId(), requestBody.newValue(), requestBody.changeFieldName())
                .thenReturn(param)));
    }

    @DeleteMapping("delete-course/{courseId}")
    public Mono<String> deleteCourse(@RequestHeader("Authorization") String token,
                                     @PathVariable String courseId) {
        log.info("course id {}", courseId);
        return teacherService.getOrCreateTeacher(getSubIdFromToken(token))
                .map(Param::new)
                .flatMap(param -> setCourse(param, courseId))
                .flatMap(param -> courseService.deleteById(courseId));
    }

    @PutMapping("add-material")
    public Mono<TeacherCourseEditDTO> addCourseToMaterial(@RequestHeader("Authorization") String token, @RequestBody TeacherUpdateMaterialRequestBody requestBody) {
        return toTeacherCourseEditDTO(token, requestBody.courseId(),
                Optional.of(param -> courseService.addCourseToMaterial(requestBody.courseId(), requestBody.materialId()).thenReturn(param)));
    }

    @DeleteMapping("remove-material")
    public Mono<TeacherCourseEditDTO> removeCourseFromMaterial(@RequestHeader("Authorization") String token, @RequestBody TeacherUpdateMaterialRequestBody requestBody) {
        return toTeacherCourseEditDTO(token, requestBody.courseId(),
                Optional.of(param -> courseService.removeCourseFromMaterial(requestBody.courseId(), requestBody.materialId()).thenReturn(param)));
    }

    @PutMapping("add-student")
    public Mono<TeacherCourseEditDTO> addStudentsToCourse(@RequestHeader("Authorization") String token, @RequestBody TeacherUpdateStudentRequestBody requestBody) {
        return toTeacherCourseEditDTO(token, requestBody.courseId(),
                Optional.of(param -> studentService.addStudentToCourse(requestBody.studentId(), requestBody.courseId()).thenReturn(param)));
    }

    @DeleteMapping("remove-student")
    public Mono<TeacherCourseEditDTO> removeStudentsFromCourse(@RequestHeader("Authorization") String token, @RequestBody TeacherUpdateStudentRequestBody requestBody) {
        return toTeacherCourseEditDTO(token, requestBody.courseId(),
                Optional.of(param -> studentService.removeStudentFromCourse(requestBody.studentId(), requestBody.courseId()).thenReturn(param)));
    }

    @PutMapping("add-unit")
    public Mono<TeacherCourseEditDTO> addUnitsToCourse(@RequestHeader("Authorization") String token, @RequestBody TeacherUpdateUnitsRequestBody requestBody) {
        return toTeacherCourseEditDTO(token, requestBody.courseId(),
                Optional.of(param -> unitService.addUnitToCourse(requestBody.unitId(), requestBody.courseId()).thenReturn(param)));
    }

    @DeleteMapping("remove-unit")
    public Mono<TeacherCourseEditDTO> removeUnitsFromCourse(@RequestHeader("Authorization") String token, @RequestBody TeacherUpdateUnitsRequestBody requestBody) {
        return toTeacherCourseEditDTO(token, requestBody.courseId(),
                Optional.of(param -> unitService.removeUnitFromCourse(requestBody.unitId(), requestBody.courseId()).thenReturn(param)));
    }

    @PostMapping("duplicate/{courseId}")
    public Mono<String> duplicateCourse(@RequestHeader("Authorization") String token, @PathVariable("courseId") String courseId) {
        return Mono.empty();
    }


    private Mono<Param> setCourse(Param param, String courseId) {
        return courseService.findCourseByIdWithRelations(courseId).doOnNext(param::setCourse).thenReturn(param);
    }

    private Mono<Param> setAllCourses(Param param) {
        return courseService.findAllWithRelations().collectList().doOnNext(param::setAllCourses).thenReturn(param);
    }

    private Mono<Param> setUnits(Param param) {
        return unitService.findAllWithRelations(param.getCourse())
                .collectList()
                .doOnNext(param::setUnits)
                .thenReturn(param);
    }

    private Mono<Param> setAllUnits(Param param) {
        return unitService.findAllWithRelations()
                .collectList()
                .doOnNext(param::setAllUnits)
                .thenReturn(param);
    }

    private Mono<Param> setMaterials(Param param) {
        return materialService.findAllWithRelations(param.getCourse())
                .collectList()
                .doOnNext(param::setMaterials)
                .thenReturn(param);
    }

    private Mono<Param> setAllMaterials(Param param) {
        return materialService.findAllWithRelations().collectList().doOnNext(param::setAllMaterials).thenReturn(param);
    }

    private Mono<Param> setAllStudents(Param param) {
        return studentService.findAllWithRelations().collectList().doOnNext(param::setAllStudents).thenReturn(param);
    }

    private Mono<Param> setStudentsAddedInCourse(Param param) {
        return studentService.findAllWithRelations(param.getCourse())
                .collectList()
                .doOnNext(param::setStudentsAddedInCourse)
                .thenReturn(param);
    }

    private Mono<Param> setTags(Param param) {
        return tagService.findAllWithRelations().collectList().doOnNext(param::setTags).thenReturn(param);
    }

    private Mono<TeacherCourseEditDTO> toTeacherCourseEditDTO(String token, String courseId, Optional<Function<Param, Mono<Param>>> additionalProcessing) {
        return teacherService.getOrCreateTeacher(getSubIdFromToken(token))
                .map(Param::new)
                .flatMap(additionalProcessing.orElse(Mono::just))
                .flatMap(param -> setCourse(param, courseId))
                .flatMap(param -> Mono.zip(
                                setAllCourses(param),
                                setUnits(param),
                                setAllUnits(param),
                                setMaterials(param),
                                setAllMaterials(param),
                                setAllStudents(param),
                                setStudentsAddedInCourse(param),
                                setTags(param))
                        .thenReturn(param))
                .map(TeacherCourseEditViewController::toDTO);
    }

    private static TeacherCourseEditDTO toDTO(Param param) {
        return new TeacherCourseEditDTO(
                param.getCourse().getId(),
                param.getCourse().getName(),
                param.getCourse().getDescription(),
                formatDateDTO(param.getCourse().getStartDate()),
                formatDateDTO(param.getCourse().getEndDate()),
                param.getCourse().getStatus().toString(),
                param.getUnits().stream()
                        .map(unit -> TeacherCourseEditViewController.toDTO(unit, Optional.of(param::isUnitAttachedToCourse)))
                        .sorted(Comparator.comparing(TeacherCourseEditDTO.Unit::attachedToCourse).reversed())
                        .toArray(TeacherCourseEditDTO.Unit[]::new),
                param.getAllUnits().stream()
                        .sorted(Comparator.comparing(Unit::getName))
                        .map(unit -> TeacherCourseEditViewController.toDTO(unit, Optional.of(param::isUnitAttachedToCourse)))
                        .sorted(Comparator.comparing(TeacherCourseEditDTO.Unit::attachedToCourse).reversed())
                        .toArray(TeacherCourseEditDTO.Unit[]::new),
                param.getMaterials().stream()
                        .map(material -> TeacherCourseEditViewController.toDTO(material, Optional.of(param::isMaterialAttachedToCourse), Optional.of(param::getCourseByMaterialRelationId), Optional.empty()))
                        .toArray(TeacherCourseEditDTO.Material[]::new),
                param.getAllMaterials().stream()
                        .map(material -> TeacherCourseEditViewController.toDTO(material, Optional.of(param::isMaterialAttachedToCourse), Optional.of(param::getCourseByMaterialRelationId), Optional.empty()))
                        .sorted(Comparator.comparing(TeacherCourseEditDTO.Material::attachedToCourse).reversed())
                        .toArray(TeacherCourseEditDTO.Material[]::new),
                param.getAllStudents().stream()
                        .map(student -> TeacherCourseEditViewController.toDTO(student, Optional.of(param::isStudentAttachedToCourse)))
                        .sorted(Comparator.comparing(TeacherCourseEditDTO.Student::attachedToCourse).reversed())
                        .toArray(TeacherCourseEditDTO.Student[]::new),
                param.getStudentsAddedInCourse().stream()
                        .map(student -> TeacherCourseEditViewController.toDTO(student, Optional.of(param::isStudentAttachedToCourse)))
                        .toArray(TeacherCourseEditDTO.Student[]::new)
        );
    }

    private static String formatDateDTO(LocalDate localDate) {
        String pattern = "MM/dd/yyyy";
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);
        return localDate.format(dateTimeFormatter);
    }

    private static TeacherCourseEditDTO.Unit toDTO(Unit unit, Optional<Function<Unit, Boolean>> isAttachedToUnit) {
        return new TeacherCourseEditDTO.Unit(
                unit.getId(),
                unit.getName(),
                isAttachedToUnit.map(func -> func.apply(unit)).orElse(false));
    }

    private static TeacherCourseEditDTO.Material toDTO(Material material,
                                                       Optional<Function<List<String>, Boolean>> isAttachedToCourse,
                                                       Optional<Function<String, Optional<Course>>> courseByRelationId,
                                                       Optional<Function<String, Optional<Tag>>> tagsAttached
    ) {
        return new TeacherCourseEditDTO.Material(
                material.getId(),
                material.getName(),
                material.getUri(),
                isAttachedToCourse.map(func -> func.apply(material.getMaterialRelations())).orElse(false),
                material.getMaterialRelations().stream()
                        .map(courseByRelationId.orElse(id -> Optional.empty()))
                        .flatMap(Optional::stream)
                        .map(TeacherCourseEditViewController::toDTO)
                        .toArray(TeacherCourseEditDTO.Course[]::new),
                material.getMaterialRelations().stream()
                        .map(tagsAttached.orElse(id -> Optional.empty()))
                        .flatMap(Optional::stream)
                        .map(TeacherCourseEditViewController::toDTO)
                        .toArray(TeacherCourseEditDTO.Tag[]::new),
                material.getMaterialType().toString()
        );
    }

    private static TeacherCourseEditDTO.Student toDTO(Student student,
                                                      Optional<Function<List<Course>, Boolean>> isAttachedToCourse) {
        return new TeacherCourseEditDTO.Student(
                student.getId(),
                student.getName(),
                isAttachedToCourse.map(function -> function.apply(student.getCourses())).orElse(false),
                student.getCourses().stream().map(TeacherCourseEditViewController::toDTO).toArray(TeacherCourseEditDTO.Course[]::new)
        );
    }

    private static TeacherCourseEditDTO.Course toDTO(Course course) {
        return new TeacherCourseEditDTO.Course(
                course.getId(),
                course.getName());
    }

    private static TeacherCourseEditDTO.Tag toDTO(Tag tag) {
        return new TeacherCourseEditDTO.Tag(
                tag.getId(),
                tag.getName());
    }

    @Data
    private static class Param {
        Teacher teacher;
        Course course;
        List<Unit> units = new ArrayList<>();
        List<Unit> allUnits = new ArrayList<>();
        List<Material> materials = new ArrayList<>();
        List<Material> allMaterials = new ArrayList<>();
        List<Student> allStudents = new ArrayList<>();
        List<Student> studentsAddedInCourse = new ArrayList<>();
        List<Course> allCourses = new ArrayList<>();
        List<Tag> tags = new ArrayList<>();

        public Param(Teacher teacher) {
            this.teacher = teacher;
        }

        public Optional<Course> getCourseByMaterialRelationId(String materialRelationId) {
            return allCourses.stream()
                    .filter(course -> course.getMaterialRelations().stream()
                            .anyMatch(relationId -> relationId.equals(materialRelationId)))
                    .findAny();
        }

        public Boolean isUnitAttachedToCourse(Unit unit) {
            return course.getUnits().stream().anyMatch(u -> u.getId().equals(unit.getId()));
        }

        public Boolean isStudentAttachedToCourse(List<Course> courses) {
            return courses.stream().anyMatch(c -> c.getId().equals(course.getId()));
        }

        public Boolean isMaterialAttachedToCourse(List<String> materialRelationIds) {
            return materialRelationIds.stream().anyMatch(s -> course.getMaterialRelations().contains(s));
        }
    }
}

