package chaincue.tech.r2dbcbackend2.masters.unit_master;

import chaincue.tech.r2dbcbackend2.exeptions.RelationAlreadyExistsException;
import chaincue.tech.r2dbcbackend2.exeptions.RelationNotFoundException;
import chaincue.tech.r2dbcbackend2.exeptions.UnitNotFoundException;
import chaincue.tech.r2dbcbackend2.masters.assignment_master.repositories.AssignmentRepository;
import chaincue.tech.r2dbcbackend2.masters.course_master.Course;
import chaincue.tech.r2dbcbackend2.masters.course_master.repositories.CourseRepository;
import chaincue.tech.r2dbcbackend2.masters.material_master.MaterialRelation;
import chaincue.tech.r2dbcbackend2.masters.material_master.repositories.MaterialRelationsRepository;
import chaincue.tech.r2dbcbackend2.masters.material_master.repositories.MaterialRepository;
import chaincue.tech.r2dbcbackend2.masters.tag_master.Tag;
import chaincue.tech.r2dbcbackend2.masters.tag_master.TagRepository;
import chaincue.tech.r2dbcbackend2.masters.unit_master.repositories.UnitCourseRelationRepository;
import chaincue.tech.r2dbcbackend2.masters.unit_master.repositories.UnitRepository;
import chaincue.tech.r2dbcbackend2.masters.unit_master.repositories.UnitTagRelationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class UnitService implements UnitContract {
    private final UnitRepository unitRepository;
    private final CourseRepository courseRepository;
    private final MaterialRepository materialRepository;
    private final TagRepository tagRepository;
    private final UnitCourseRelationRepository unitCourseRelationRepository;
    private final MaterialRelationsRepository materialRelationsRepository;
    private final UnitTagRelationRepository unitTagRelationRepository;
    private final AssignmentRepository assignmentRepository;

    @Transactional
    @Override
    public Mono<Unit> save(String name) {
        var unit = Unit.create(Objects.equals(name, "") ? "default" : name);
        return unitRepository.save(unit);
    }

    @Override
    public Mono<UnitCourseRelation> addUnitToCourse(String unitId, String courseId) {
        return unitCourseRelationRepository.existsByUnitIdAndCourseId(unitId, courseId)
                .flatMap(doesRelationExist -> {
                    if (doesRelationExist) {
                        String errorMessage = String.format("Relation already exists for Unit ID %s and Course ID %s", unitId, courseId);
                        return Mono.error(new RelationAlreadyExistsException(errorMessage));
                    }
                    return Mono.just(UnitCourseRelation.create(unitId, courseId));
                })
                .flatMap(unitCourseRelationRepository::save);
    }

    @Override
    public Mono<UnitCourseRelation> removeUnitFromCourse(String unitId, String courseId) {
        return unitCourseRelationRepository.findByUnitIdAndCourseId(unitId, courseId)
                .switchIfEmpty(Mono.error(new RelationNotFoundException(unitId)))
                .flatMap(unitCourseRelation -> unitCourseRelationRepository.deleteById(unitCourseRelation.getId())
                        .thenReturn(unitCourseRelation));
    }

    @Override
    public Mono<UnitTagRelation> addUnitToTag(String unitId, String tagId) {
        return unitTagRelationRepository.existsByUnitIdAndTagId(unitId, tagId)
                .flatMap(doesRelationExist -> {
                    if (doesRelationExist) {
                        String errorMessage = String.format("Relation already exists for Unit ID %s and Tag ID %s", unitId, tagId);
                        return Mono.error(new RelationAlreadyExistsException(errorMessage));
                    }
                    return Mono.just(UnitTagRelation.create(unitId, tagId));
                })
                .flatMap(unitTagRelationRepository::save);
    }

    @Override
    public Mono<MaterialRelation> addUnitToMaterial(String unitId, String materialId) {
        return materialRelationsRepository.existsByMaterialIdAndUnitId(materialId, unitId)
                .flatMap(doesRelationExist -> {
                    if (doesRelationExist) {
                        String errorMessage = String.format("Relation already exists for Material ID %s and Unit ID %s", materialId, unitId);
                        return Mono.error(new RelationAlreadyExistsException(errorMessage));
                    }
                    return Mono.just(MaterialRelation.create(materialId, unitId, MaterialRelation.RelationType.UNIT));
                })
                .flatMap(materialRelationsRepository::save);
    }

    @Transactional
    @Override
    public Mono<Unit> findUnitByIdWithRelations(String unitId) {
        return unitRepository.findById(unitId)
                .switchIfEmpty(Mono.error(new UnitNotFoundException(unitId)))
                .flatMap(unit -> Mono.zip(
                                fetchCoursesForUnit(unit),
                                fetchMaterialRelationForUnit(unit),
                                fetchAssignmentsForUnit(unit),
                                fetchTagRelationForUnit(unit))
                        .thenReturn(unit));
    }

    @Override
    public Flux<Unit> findAllWithRelations() {
        return unitRepository.findAll()
                .flatMap(unit -> Mono.zip(
                                fetchCoursesForUnit(unit),
                                fetchMaterialRelationForUnit(unit),
                                fetchAssignmentsForUnit(unit),
                                fetchTagRelationForUnit(unit)
                        )
                        .thenReturn(unit));
    }

    @Override
    public Flux<Unit> findAllWithRelations(Course course) {
        return unitCourseRelationRepository.findByCourseId(course.getId())
                .flatMap(relation -> unitRepository.findById(relation.getUnitId())
                        .flatMap(unit -> Mono.zip(
                                        fetchCoursesForUnit(unit),
                                        fetchMaterialRelationForUnit(unit),
                                        fetchAssignmentsForUnit(unit),
                                        fetchTagRelationForUnit(unit)
                                )
                                .thenReturn(unit))
                );
    }

    @Override
    public Flux<Unit> findByNameContainingIgnoreCase(String[] searchValues) {
        return unitRepository.findAll()
                .filter(unit -> {
                    if (searchValues == null || searchValues.length == 0) return true;
                    return Arrays.stream(searchValues).anyMatch(val -> unit.getName().toLowerCase().contains(val.toLowerCase()));
                })
                .flatMap(unit -> Mono.zip(
                                fetchCoursesForUnit(unit),
                                fetchMaterialRelationForUnit(unit),
                                fetchTagRelationForUnit(unit)
                        )
                        .thenReturn(unit));
    }

    private Mono<Unit> fetchCoursesForUnit(Unit unit) {
        return unitCourseRelationRepository.findByUnitId(unit.getId())
                .map(UnitCourseRelation::getCourseId)
                .collectList()
                .flatMap(this::findCoursesByIds)
                .doOnNext(unit::setCourses)
                .thenReturn(unit);
    }

    private Mono<Unit> fetchAssignmentsForUnit(Unit unit) {
        return assignmentRepository.findAllByUnitId(unit.getId())
                .collectList()
                .doOnNext(unit::setAssignments)
                .thenReturn(unit);
    }

    private Mono<Unit> fetchMaterialRelationForUnit(Unit unit) {
        return materialRelationsRepository.findByUnitId(unit.getId())
                .mapNotNull(MaterialRelation::getId)
                .collectList()
                .doOnNext(unit::setMaterialRelations)
                .thenReturn(unit);
    }

    private Mono<Unit> fetchTagRelationForUnit(Unit unit) {
        return unitTagRelationRepository.findByUnitId(unit.getId())
                .mapNotNull(UnitTagRelation::getTagId)
                .collectList()
                .flatMap(this::findTagsByIds)
                .doOnNext(unit::setTags)
                .thenReturn(unit);
    }

    private Mono<List<Course>> findCoursesByIds(List<String> courseIds) {
        return courseRepository.findAllById(courseIds).collectList();
    }

    private Mono<List<Tag>> findTagsByIds(List<String> courseIds) {
        return tagRepository.findAllById(courseIds).collectList();
    }

    @Override
    @Transactional
    public Mono<Unit> updateUnitField(String unitId, String newValue, Unit.ChangeFieldName changeFieldName) {
        return unitRepository.findById(unitId)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Unit with id " + unitId + " not found")))
                .flatMap(unit -> {
                    switch (changeFieldName) {
                        case TITLE -> {
                            unit.setName(newValue);
                            return unitRepository.updateUnitName(unitId, newValue)
                                    .thenReturn(unit);
                        }
                        case DESCRIPTION -> {
                            unit.setDescription(newValue);
                            return unitRepository.updateUnitDescription(unitId, newValue)
                                    .thenReturn(unit);
                        }
                        default -> {
                            return Mono.error(new IllegalArgumentException("Invalid ChangeFieldName: " + changeFieldName));
                        }
                    }
                });
    }
}
