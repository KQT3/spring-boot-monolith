package chaincue.tech.r2dbcbackend2.views.teacher_view.unit_edit;

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
import chaincue.tech.r2dbcbackend2.views.teacher_view.unit_edit.DTOs.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static chaincue.tech.r2dbcbackend2.utilities.JWTDecoderUtil.getSubIdFromToken;


@RestController
@RequestMapping("teacher-unit-edit")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class TeacherUnitEditViewController {
    private final UnitService unitService;
    private final TeacherService teacherService;
    private final AssignmentService assignmentService;
    private final MaterialService materialService;
    private final CourseService courseService;
    private final TagService tagService;

    @GetMapping("{unitId}")
    public Mono<TeacherUnitEditDTO> unitEditView(@RequestHeader("Authorization") String token, @PathVariable String unitId) {
        log.info("unit id {}", unitId);
        return toUnitEditDTO(token, unitId, Optional.empty());
    }

    @PutMapping("update-unit-field")
    public Mono<TeacherUnitEditDTO> updateUnitField(@RequestHeader("Authorization") String token, @RequestBody TeacherUpdateUnitFieldDTO requestBody) {
        return toUnitEditDTO(token, requestBody.unitId(), Optional.of(param -> unitService.updateUnitField(requestBody.unitId(), requestBody.newValue(), requestBody.changeFieldName())
                .thenReturn(param)));
    }

    @DeleteMapping("delete-assignment/{unitId}/{assignmentId}")
    public Mono<TeacherUnitEditDTO> removeAssignment(@RequestHeader("Authorization") String token, @PathVariable String unitId, @PathVariable String assignmentId) {
        return Mono.empty();
    }

//    @PutMapping("add-material")
//    public Mono<TeacherCourseEditDTO> addCourseToMaterial(@RequestHeader("Authorization") String token, @RequestBody se.sensera.lambda.backend.views.teacher_portal.course_edit.DTOs.TeacherUpdateMaterialRequestBody requestBody) {
//        return toTeacherCourseEditDTO(token, requestBody.courseId(),
//                Optional.of(param -> courseHelper.addCourseToMaterial(requestBody.courseId(), requestBody.materialId()).thenReturn(param)));
//    }

    @PutMapping("add-tag")
    public Mono<TeacherUnitEditDTO> addTag(@RequestHeader("Authorization") String token, @RequestBody TeacherTagDTORequestBody addTagDTO) {
        log.info("addTagDTO: {}", addTagDTO);
        return Mono.empty();
    }

    @DeleteMapping("remove-tag")
    public Mono<TeacherUnitEditDTO> removeTag(@RequestHeader("Authorization") String token, @RequestBody TeacherTagDTORequestBody addTagDTO) {
        log.info("addTagDTO: {}", addTagDTO);
        return Mono.empty();
    }

    @PutMapping("add-materials")
    public Mono<TeacherUnitEditDTO> addUnitToMaterial(@RequestHeader("Authorization") String token, @RequestBody TeacherUpdateMaterialRequestBody requestBody) {
        log.info("updateMaterialRequestBody: {}", requestBody);
        return toUnitEditDTO(token, requestBody.unitId(), Optional.of(param -> unitService.addUnitToMaterial(requestBody.unitId(), requestBody.materialId())
                .thenReturn(param)));
    }

    @DeleteMapping("remove-materials")
    public Mono<TeacherUnitEditDTO> removeMaterialsFromUnit(@RequestHeader("Authorization") String token, @RequestBody TeacherUpdateMaterialRequestBody teacherUpdateMaterialRequestBody) {
        log.info("updateMaterialRequestBody: {}", teacherUpdateMaterialRequestBody);
        return Mono.empty();
    }

    @PostMapping("search-material/{unitId}")
    public Flux<TeacherUnitEditDTO.Material> searchMaterial(@RequestHeader("Authorization") String token, @PathVariable String unitId, @RequestBody TeacherUnitEditSearchValues searchValues) {
        log.info("searchValues: {}", searchValues);
        return Flux.empty();
    }

    private Mono<Param> filterMaterialsBySearchValues(TeacherUnitEditSearchValues searchValues, Param param) {
        return Mono.empty();
    }

    @PostMapping("create-assignment")
    public Mono<TeacherUnitEditDTO> createAssignment(@RequestHeader("Authorization") String token,
                                                     @RequestBody TeacherCreateAssignmentReqBody teacherCreateAssignmentReqBody) {
        log.info("createAssignmentReqBody: {}", teacherCreateAssignmentReqBody);
        return Mono.empty();
    }

    private Mono<TeacherUnitEditDTO> toUnitEditDTO(String token, String unitId, Optional<Function<Param, Mono<Param>>> additionalProcessing) {
        return teacherService.getOrCreateTeacher(getSubIdFromToken(token))
                .map(Param::new)
                .flatMap(additionalProcessing.orElse(Mono::just))
                .flatMap(param -> setUnit(param, unitId))
                .flatMap(param -> Mono.zip(
                                setAssignments(param),
                                setCourses(param),
                                setMaterials(param),
                                setAllMaterials(param),
                                setTags(param),
                                setAllTags(param)
                        )
                        .thenReturn(param))
                .map(TeacherUnitEditViewController::toDTO);
    }

    private static TeacherUnitEditDTO toDTO(Param param) {
        return new TeacherUnitEditDTO(
                param.getUnit().getId(),
                param.getUnit().getName(),
                param.getUnit().getDescription(),
                param.getAssignments().stream().map(TeacherUnitEditViewController::toDTO).toArray(TeacherUnitEditDTO.Assignment[]::new),
                param.getCourses().stream().map(TeacherUnitEditViewController::toDTO).toArray(TeacherUnitEditDTO.Course[]::new),
                param.getMaterials().stream()
                        .sorted(Comparator.comparing(Material::getName))
                        .map(material -> TeacherUnitEditViewController.toDTO(material, Optional.empty(), Optional.empty(), Optional.empty()))
                        .toArray(TeacherUnitEditDTO.Material[]::new),
                param.getAllMaterials().stream()
                        .map(material -> TeacherUnitEditViewController.toDTO(material, Optional.empty(), Optional.empty(), Optional.of(param::tagsAttached)))
                        .sorted(Comparator.comparing(TeacherUnitEditDTO.Material::attachedToCourse).reversed())
                        .toArray(TeacherUnitEditDTO.Material[]::new),
                param.getTagsAttached().stream().map(TeacherUnitEditViewController::toDTO).toArray(TeacherUnitEditDTO.Tag[]::new),
                param.getAllTagsNotAttached().stream().map(TeacherUnitEditViewController::toDTO).toArray(TeacherUnitEditDTO.Tag[]::new)
        );
    }


    private static TeacherUnitEditDTO.Assignment toDTO(Assignment assignment) {
        return new TeacherUnitEditDTO.Assignment(assignment.getId(), assignment.getName());
    }

    private static TeacherUnitEditDTO.Material toDTO(Material material,
                                                     Optional<Function<List<String>, Boolean>> isAttachedToUnit,
                                                     Optional<Function<String, Optional<Course>>> courseByRelationId,
                                                     Optional<Function<List<String>, List<Tag>>> tagsAttached) {
        return new TeacherUnitEditDTO.Material(
                material.getId(),
                material.getName(),
                material.getUri(),
                isAttachedToUnit.map(function -> function.apply(material.getMaterialRelations())).orElse(false),
                material.getMaterialRelations().stream()
                        .map(courseByRelationId.orElse(id -> Optional.empty()))
                        .flatMap(Optional::stream)
                        .map(TeacherUnitEditViewController::toDTO)
                        .toArray(TeacherUnitEditDTO.Course[]::new),
                tagsAttached.map(function -> function.apply(material.getMaterialRelations())).orElse(Collections.emptyList()).stream()
                        .map(TeacherUnitEditViewController::toDTO)
                        .toArray(TeacherUnitEditDTO.Tag[]::new),
                material.getMaterialType().toString());
    }

    private static TeacherUnitEditDTO.Course toDTO(Course course) {
        return new TeacherUnitEditDTO.Course(course.getId(), course.getName());
    }

    private static TeacherUnitEditDTO.Tag toDTO(Tag tag) {
        return new TeacherUnitEditDTO.Tag(tag.getId(), tag.getName());
    }

    private Mono<Param> setUnit(Param param, String unitId) {
        return unitService.findUnitByIdWithRelations(unitId).doOnNext(param::setUnit).thenReturn(param);
    }

    private Mono<Param> setAssignments(Param param) {
        return assignmentService.findAllWithRelations(param.getUnit()).collectList().doOnNext(param::setAssignments).thenReturn(param);
    }

    private Mono<Param> setCourses(Param param) {
        return courseService.findAllWithRelations(param.getUnit()).collectList().doOnNext(param::setCourses).thenReturn(param);
    }

    private Mono<Param> setMaterials(Param param) {
        return materialService.findAllWithRelations(param.getUnit()).collectList().doOnNext(param::setMaterials).thenReturn(param);
    }

    private Mono<Param> setAllMaterials(Param param) {
        return materialService.findAllWithRelations().collectList().doOnNext(param::setAllMaterials).thenReturn(param);
    }

    private Mono<Param> setTags(Param param) {
        return tagService.findAllWithRelations(param.getUnit()).collectList().doOnNext(param::setTagsAttached).thenReturn(param);
    }

    private Mono<Param> setAllTags(Param param) {
        return tagService.findAllWithRelations().collectList().doOnNext(param::setAllTags).thenReturn(param);
    }

    @Data
    private static class Param {
        private Teacher teacher;
        private Unit unit;
        private List<Assignment> assignments = new ArrayList<>();
        private List<Material> materials = new ArrayList<>();
        private List<Material> allMaterials = new ArrayList<>();
        private List<Course> courses = new ArrayList<>();
        private List<Tag> tagsAttached = new ArrayList<>();
        private List<Tag> allTags = new ArrayList<>();

        public Param(Teacher teacher) {
            this.teacher = teacher;
        }

        List<Tag> getAllTagsNotAttached() {
            return allTags.stream().filter(tag -> !tagsAttached.contains(tag)).collect(Collectors.toList());
        }

//        boolean isAttachedToUnit(List<String> materialRelationIds) {
//            return materialRelations.stream().map(MaterialRelation::getId).anyMatch(materialRelationIds::contains);
//        }
//
//        Optional<Course> getCourseByMaterialRelationId(String materialRelationId) {
//            return allMaterialRelations.stream()
//                    .filter(materialRelation -> materialRelation.getRelationType().equals(MaterialRelation.RelationType.COURSE))
//                    .filter(materialRelation -> materialRelation.getId().equals(materialRelationId))
//                    .flatMap(materialRelation -> allCourses.stream().filter(course -> course.getId().equals(materialRelation.getCourseId())))
//                    .findAny();
//        }

        List<Tag> tagsAttached(List<String> identifier) {
            return allTags.stream().filter(tag -> identifier.contains(tag.getId())).collect(Collectors.toList());
        }
    }
}
