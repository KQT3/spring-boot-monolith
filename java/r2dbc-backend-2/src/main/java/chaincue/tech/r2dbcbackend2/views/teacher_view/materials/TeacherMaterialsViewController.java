package chaincue.tech.r2dbcbackend2.views.teacher_view.materials;

import chaincue.tech.r2dbcbackend2.masters.assignment_master.Assignment;
import chaincue.tech.r2dbcbackend2.masters.assignment_master.AssignmentService;
import chaincue.tech.r2dbcbackend2.masters.course_master.Course;
import chaincue.tech.r2dbcbackend2.masters.course_master.CourseService;
import chaincue.tech.r2dbcbackend2.masters.material_master.Material;
import chaincue.tech.r2dbcbackend2.masters.material_master.MaterialService;
import chaincue.tech.r2dbcbackend2.masters.tag_master.Tag;
import chaincue.tech.r2dbcbackend2.masters.tag_master.TagService;
import chaincue.tech.r2dbcbackend2.masters.teacher_master.Teacher;
import chaincue.tech.r2dbcbackend2.masters.teacher_master.TeacherService;
import chaincue.tech.r2dbcbackend2.masters.unit_master.Unit;
import chaincue.tech.r2dbcbackend2.masters.unit_master.UnitService;
import chaincue.tech.r2dbcbackend2.views.teacher_view.materials.DTOs.TeacherSearchValues;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static chaincue.tech.r2dbcbackend2.utilities.JWTDecoderUtil.getSubIdFromToken;

@RestController
@RequestMapping("teacher-materials")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Slf4j
public class TeacherMaterialsViewController {
    private final TeacherService teacherService;
    private final MaterialService materialService;
    private final UnitService unitService;
    private final CourseService courseService;
    private final AssignmentService assignmentService;
    private final TagService tagService;

    @GetMapping
    public Mono<TeacherMaterialsViewDTO> materialView(@RequestHeader("Authorization") String token) {
        return teacherService.getOrCreateTeacher(getSubIdFromToken(token))
                .map(Param::new)
                .flatMap(param -> Mono.zip(
                        setMaterials(param),
                        setCourses(param),
                        setUnits(param),
                        setAssignments(param),
                        setTags(param)
                ).thenReturn(param))
                .map(TeacherMaterialsViewController::toDTO);
    }

    @PostMapping("search-material")
    public Mono<TeacherMaterialsViewDTO> searchMaterial(@RequestHeader("Authorization") String token, @RequestBody TeacherSearchValues searchValues) {
        return teacherService.getOrCreateTeacher(getSubIdFromToken(token))
                .map(Param::new)
                .flatMap(param -> Mono.zip(
                        filterMaterialBySearchValues(searchValues, param),
                        setCourses(param),
                        setUnits(param),
                        setAssignments(param),
                        setTags(param)
                ).thenReturn(param))
                .map(TeacherMaterialsViewController::toDTO);
    }

    private Mono<Param> setMaterials(Param param) {
        return materialService.findAllWithRelations().collectList().doOnNext(param::setMaterials).thenReturn(param);
    }

    private Mono<Param> filterMaterialBySearchValues(TeacherSearchValues searchValues, Param param) {
        return materialService.findAllWithRelations()
                .filter(material -> {
                    if (searchValues.searchValues().length > 0) {
                        return Arrays.stream(searchValues.searchValues())
                                .anyMatch(s -> material.getName().toLowerCase().contains(s.toLowerCase()));
                    }
                    return true;
                })
                .collect(Collectors.toList())
                .doOnNext(param::setMaterials)
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

    private Mono<Param> setTags(Param param) {
        return tagService.findAllWithRelations().collectList().doOnNext(param::setTags).thenReturn(param);
    }

    public static TeacherMaterialsViewDTO toDTO(Param param) {
        return new TeacherMaterialsViewDTO(
                param.getTeacher().getId(),
                param.getTeacher().getName(),
                param.getMaterials().stream()
                        .map(material -> TeacherMaterialsViewController.toDTO(
                                material,
                                param::getCourseByMaterialRelationId,
                                param::getUnitByMaterialRelationId,
                                param::getAssignmentByMaterialRelationId,
                                param::getTagByMaterialTagRelationId)
                        )
                        .sorted(Comparator.comparing(TeacherMaterialsViewDTO.Material::name))
                        .toArray(TeacherMaterialsViewDTO.Material[]::new),
                param.getCourses().stream()
                        .map(TeacherMaterialsViewController::toDTO)
                        .sorted(Comparator.comparing(TeacherMaterialsViewDTO.Course::name))
                        .toArray(TeacherMaterialsViewDTO.Course[]::new),
                param.getUnits().stream()
                        .map(TeacherMaterialsViewController::toDTO)
                        .sorted(Comparator.comparing(TeacherMaterialsViewDTO.Unit::name))
                        .toArray(TeacherMaterialsViewDTO.Unit[]::new),
                param.getAssignments().stream()
                        .map(TeacherMaterialsViewController::toDTO)
                        .sorted(Comparator.comparing(TeacherMaterialsViewDTO.Assignment::name))
                        .toArray(TeacherMaterialsViewDTO.Assignment[]::new),
                param.getTags().stream()
                        .map(TeacherMaterialsViewController::toDTO)
                        .sorted(Comparator.comparing(TeacherMaterialsViewDTO.Tag::name))
                        .toArray(TeacherMaterialsViewDTO.Tag[]::new));
    }

    public static TeacherMaterialsViewDTO.Material toDTO(Material material,
                                                         Function<String, Optional<Course>> courseByRelationId,
                                                         Function<String, Optional<Unit>> unitByRelationId,
                                                         Function<String, Optional<Assignment>> assignmentByRelationId,
                                                         Function<String, Optional<Tag>> tagsByRelationId
    ) {
        return new TeacherMaterialsViewDTO.Material(
                material.getId(),
                material.getName(),
                material.getMaterialType().toString(),
                material.getDescription(),
                material.getUri(),
                material.getMaterialRelations().stream()
                        .map(courseByRelationId)
                        .flatMap(Optional::stream)
                        .map(TeacherMaterialsViewController::toDTO)
                        .toArray(TeacherMaterialsViewDTO.Course[]::new),
                material.getMaterialRelations().stream()
                        .map(unitByRelationId)
                        .flatMap(Optional::stream)
                        .map(TeacherMaterialsViewController::toDTO)
                        .toArray(TeacherMaterialsViewDTO.Unit[]::new),
                material.getMaterialRelations().stream()
                        .map(assignmentByRelationId)
                        .flatMap(Optional::stream)
                        .map(TeacherMaterialsViewController::toDTO)
                        .toArray(TeacherMaterialsViewDTO.Assignment[]::new),
                material.getMaterialRelations().stream()
                        .map(tagsByRelationId)
                        .flatMap(Optional::stream)
                        .map(TeacherMaterialsViewController::toDTO)
                        .toArray(TeacherMaterialsViewDTO.Tag[]::new)
        );
    }

    public static TeacherMaterialsViewDTO.Course toDTO(Course course) {
        return new TeacherMaterialsViewDTO.Course(
                course.getId(),
                course.getName());
    }

    public static TeacherMaterialsViewDTO.Unit toDTO(Unit unit) {
        return new TeacherMaterialsViewDTO.Unit(
                unit.getId(),
                unit.getName());
    }

    public static TeacherMaterialsViewDTO.Assignment toDTO(Assignment assignment) {
        return new TeacherMaterialsViewDTO.Assignment(
                assignment.getId(),
                assignment.getName());
    }

    public static TeacherMaterialsViewDTO.Tag toDTO(Tag tag) {
        return new TeacherMaterialsViewDTO.Tag(
                tag.getId(),
                tag.getName());
    }

    @Data
    private static class Param {
        Teacher teacher;
        List<Material> materials = new ArrayList<>();
        List<Course> courses = new ArrayList<>();
        List<Unit> units = new ArrayList<>();
        List<Assignment> assignments = new ArrayList<>();
        List<Tag> tags = new ArrayList<>();

        public Param(Teacher teacher) {
            this.teacher = teacher;
        }

        public Optional<Course> getCourseByMaterialRelationId(String materialRelationId) {
            return courses.stream()
                    .filter(course -> course.getMaterialRelations().stream()
                            .anyMatch(relationId -> relationId.equals(materialRelationId)))
                    .findAny();
        }

        public Optional<Unit> getUnitByMaterialRelationId(String materialRelationId) {
            return units.stream()
                    .filter(course -> course.getMaterialRelations().stream()
                            .anyMatch(relationId -> relationId.equals(materialRelationId)))
                    .findAny();
        }

        public Optional<Assignment> getAssignmentByMaterialRelationId(String materialRelationId) {
            return assignments.stream()
                    .filter(course -> course.getMaterialRelations().stream()
                            .anyMatch(relationId -> relationId.equals(materialRelationId)))
                    .findAny();
        }

        public Optional<Tag> getTagByMaterialTagRelationId(String materialRelationId) {
            return tags.stream()
                    .filter(course -> course.getMaterialRelations().stream()
                            .anyMatch(relationId -> relationId.equals(materialRelationId)))
                    .findAny();
        }
    }
}
