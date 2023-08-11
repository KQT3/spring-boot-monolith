package chaincue.tech.r2dbcbackend2.masters.unit_master;

import chaincue.tech.r2dbcbackend2.exeptions.CourseNotFoundException;
import chaincue.tech.r2dbcbackend2.exeptions.MaterialNotFoundException;
import chaincue.tech.r2dbcbackend2.exeptions.UnitNotFoundException;
import chaincue.tech.r2dbcbackend2.masters.course_master.Course;
import chaincue.tech.r2dbcbackend2.masters.course_master.CourseRepository;
import chaincue.tech.r2dbcbackend2.masters.material_master.MaterialRelation;
import chaincue.tech.r2dbcbackend2.masters.material_master.MaterialRelationsRepository;
import chaincue.tech.r2dbcbackend2.masters.material_master.MaterialRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@AllArgsConstructor
public class UnitHelper {
    private final UnitRepository unitRepository;
    private final CourseRepository courseRepository;
    private final MaterialRepository materialRepository;
    private final UnitCourseRelationRepository unitCourseRelationRepository;
    private final MaterialRelationsRepository materialRelationsRepository;

    @Transactional
    public Mono<Unit> save(String name) {
        var unit = Unit.create(name);
        return unitRepository.save(unit);
    }

    @Transactional
    public Mono<UnitCourseRelation> addUnitToCourse(String unitId, String courseId) {
        return Mono.zip(courseRepository.existsById(courseId), unitRepository.existsById(unitId),
                        (courseExists, unitExists) -> {
                            if (!courseExists) throw new CourseNotFoundException(courseId);
                            if (!unitExists) throw new UnitNotFoundException(unitId);
                            return UnitCourseRelation.create(unitId, courseId);
                        })
                .flatMap(unitCourseRelationRepository::save);
    }

    @Transactional
    public Mono<Unit> findUnitByIdWithRelations(String unitId) {
        return unitRepository.findById(unitId)
                .switchIfEmpty(Mono.error(new UnitNotFoundException(unitId)))
                .flatMap(unit -> Mono.zip(fetchCoursesForUnit(unit), fetchMaterialRelationForUnit(unit))
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

    private Mono<Unit> fetchMaterialRelationForUnit(Unit unit) {
        return materialRelationsRepository.findByUnitId(unit.getId())
                .mapNotNull(MaterialRelation::getId)
                .collectList()
                .doOnNext(unit::setMaterialRelations)
                .thenReturn(unit);
    }

    private Mono<List<Course>> findCoursesByIds(List<String> courseIds) {
        return courseRepository.findAllById(courseIds).collectList();
    }

    @Transactional
    public Mono<MaterialRelation> addUnitToMaterial(String unitId, String materialId) {
        var materialRelation = MaterialRelation.create(materialId, unitId, MaterialRelation.RelationType.UNIT);
        return Mono.zip(unitRepository.existsById(unitId), materialRepository.existsById(materialId),
                        (unitExists, materialExists) -> {
                            if (!unitExists) throw new UnitNotFoundException(unitId);
                            if (!materialExists) throw new MaterialNotFoundException(materialId);
                            return materialRelation;
                        })
                .flatMap(materialRelationsRepository::save);
    }
}
