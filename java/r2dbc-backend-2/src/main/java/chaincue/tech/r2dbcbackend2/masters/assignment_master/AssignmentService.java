package chaincue.tech.r2dbcbackend2.masters.assignment_master;

import chaincue.tech.r2dbcbackend2.exeptions.RelationAlreadyExistsException;
import chaincue.tech.r2dbcbackend2.exeptions.UnitNotFoundException;
import chaincue.tech.r2dbcbackend2.masters.assignment_master.repositories.AssignmentRepository;
import chaincue.tech.r2dbcbackend2.masters.material_master.MaterialRelation;
import chaincue.tech.r2dbcbackend2.masters.material_master.repositories.MaterialRelationsRepository;
import chaincue.tech.r2dbcbackend2.masters.unit_master.Unit;
import chaincue.tech.r2dbcbackend2.masters.unit_master.repositories.UnitRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Service
@AllArgsConstructor
public class AssignmentService implements AssignmentContract {
    private final AssignmentRepository assignmentRepository;
    private final MaterialRelationsRepository materialRelationsRepository;
    private final UnitRepository unitRepository;

    @Override
    public Mono<Assignment> save(String name, String unitId) {
        var assignment = Assignment.create(Objects.equals(name, "") ? "default" : name, unitId);
        return assignmentRepository.save(assignment);
    }

    @Override
    public Flux<Assignment> findAllWithRelations() {
        return assignmentRepository.findAll()
                .flatMap(this::fetchMaterialRelationForAssignment);
    }

    @Override
    public Flux<Assignment> findAllWithRelations(Unit unit) {
        return assignmentRepository.findAllByUnitId(unit.getId())
                .flatMap(this::fetchMaterialRelationForAssignment);
    }

    @Transactional
    @Override
    public Mono<MaterialRelation> addAssignmentToMaterial(String assignmentId, String materialId) {
        return materialRelationsRepository.existsByMaterialIdAndAssignmentId(materialId, assignmentId)
                .flatMap(doesRelationExist -> {
                    if (doesRelationExist) {
                        String errorMessage = String.format("Relation already exists for Material ID %s and Assignment ID %s", materialId, assignmentId);
                        return Mono.error(new RelationAlreadyExistsException(errorMessage));
                    }
                    return Mono.just(MaterialRelation.create(materialId, assignmentId, MaterialRelation.RelationType.ASSIGNMENT));
                })
                .flatMap(materialRelationsRepository::save);
    }

    @Transactional
    @Override
    public Mono<Assignment> attachAssignmentToUnit(String assignmentId, String unitId) {
        return Mono.zip(unitRepository.existsById(unitId), assignmentRepository.findById(assignmentId))
                .flatMap(tuple -> {
                    Boolean unitExists = tuple.getT1();
                    Assignment existingAssignment = tuple.getT2();
                    if (!unitExists) {
                        String errorMessage = String.format("Unit with ID %s does not exist.", unitId);
                        return Mono.error(new UnitNotFoundException(errorMessage));
                    }
                    if (existingAssignment.getUnitId() != null && existingAssignment.getUnitId().equals(unitId)) {
                        String errorMessage = String.format("Assignment ID %s already has the Unit ID %s attached.", assignmentId, unitId);
                        return Mono.error(new RelationAlreadyExistsException(errorMessage));
                    }

                    existingAssignment.setUnitId(unitId);
                    return assignmentRepository.attachAssignmentToUnit(assignmentId, unitId).thenReturn(existingAssignment);
                });
    }

    private Mono<Assignment> fetchMaterialRelationForAssignment(Assignment assignment) {
        return materialRelationsRepository.findByAssignmentId(assignment.getId())
                .mapNotNull(MaterialRelation::getId)
                .collectList()
                .doOnNext(assignment::setMaterialRelations)
                .thenReturn(assignment);
    }
}
