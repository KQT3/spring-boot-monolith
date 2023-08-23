package chaincue.tech.r2dbcbackend2.masters.assignment_master;

import chaincue.tech.r2dbcbackend2.exeptions.RelationAlreadyExistsException;
import chaincue.tech.r2dbcbackend2.exeptions.UnitNotFoundException;
import chaincue.tech.r2dbcbackend2.masters.assignment_master.repositories.AssignmentRepository;
import chaincue.tech.r2dbcbackend2.masters.material_master.MaterialRelation;
import chaincue.tech.r2dbcbackend2.masters.material_master.repositories.MaterialRelationsRepository;
import chaincue.tech.r2dbcbackend2.masters.material_master.repositories.MaterialRepository;
import chaincue.tech.r2dbcbackend2.masters.unit_master.Unit;
import chaincue.tech.r2dbcbackend2.masters.unit_master.repositories.UnitRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AssignmentServiceTest {
    @InjectMocks
    AssignmentService assignmentService;
    @Mock
    AssignmentRepository assignmentRepository;
    @Mock
    MaterialRelationsRepository materialRelationsRepository;
    @Mock
    MaterialRepository materialRepository;
    @Mock
    UnitRepository unitRepository;

    @Test
    void save_ShouldReturnSavedAssignment_WhenInputsAreValid() {
        // Given
        String validName = "TestAssignment";
        String validUnitId = "12345";
        Assignment mockAssignment = Assignment.create(validName, validUnitId);

        when(assignmentRepository.save(any(Assignment.class))).thenReturn(Mono.just(mockAssignment));

        // When & Then
        StepVerifier.create(assignmentService.save(validName, validUnitId))
                .assertNext(assignment -> {
                    assertEquals(validName, assignment.getName());
                    assertEquals(validUnitId, assignment.getUnitId());
                    verify(assignmentRepository).save(any(Assignment.class));
                })
                .verifyComplete();
    }

    @Test
    void addAssignmentToMaterial_ShouldSaveRelation_WhenNoExistingRelation() {
        // Given
        String assignmentId = "assignment123";
        String materialId = "material123";
        var materialRelation = MaterialRelation.create(materialId, assignmentId, MaterialRelation.RelationType.ASSIGNMENT);

        when(materialRelationsRepository.existsByMaterialIdAndAssignmentId(anyString(), anyString())).thenReturn(Mono.just(false));
        when(materialRelationsRepository.save(any(MaterialRelation.class))).thenReturn(Mono.just(materialRelation));

        // When & Then
        StepVerifier.create(assignmentService.addAssignmentToMaterial(assignmentId, materialId))
                .assertNext(relation -> {
                    assertEquals(materialId, relation.getMaterialId());
                    assertEquals(assignmentId, relation.getAssignmentId());
                    assertEquals(MaterialRelation.RelationType.ASSIGNMENT, relation.getRelationType());
                })
                .verifyComplete();
    }

    @Test
    void addAssignmentToMaterial_ShouldThrowException_WhenRelationAlreadyExists() {
        // Given
        String assignmentId = "assignment123";
        String materialId = "material123";

        when(materialRelationsRepository.existsByMaterialIdAndAssignmentId(materialId, assignmentId)).thenReturn(Mono.just(true));

        // When & Then
        StepVerifier.create(assignmentService.addAssignmentToMaterial(assignmentId, materialId))
                .expectErrorMatches(throwable -> throwable instanceof RelationAlreadyExistsException
                        && throwable.getMessage().contains("Relation already exists for Material ID"))
                .verify();
    }

    @Test
    void findAllWithRelations_ShouldReturnAssignmentsWithRelations() {
        // Given
        Assignment assignment1 = Assignment.create("Assignment1", "unit123");
        Assignment assignment2 = Assignment.create("Assignment2", "unit456");
        MaterialRelation materialRelation1 = MaterialRelation.create("materialId", assignment1.getId(), MaterialRelation.RelationType.ASSIGNMENT);
        MaterialRelation materialRelation2 = MaterialRelation.create("materialId", assignment2.getId(), MaterialRelation.RelationType.ASSIGNMENT);

        when(assignmentRepository.findAll()).thenReturn(Flux.just(assignment1, assignment2));
        when(materialRelationsRepository.findByAssignmentId(anyString())).thenReturn(Flux.just(materialRelation1, materialRelation2));

        // When & Then
        StepVerifier.create(assignmentService.findAllWithRelations())
                .assertNext(assignment -> assertEquals("Assignment1", assignment.getName()))
                .assertNext(assignment -> assertEquals("Assignment2", assignment.getName()))
                .verifyComplete();
    }

    @Test
    void findAllWithRelationsWithUnit_ShouldReturnAssignmentsWithRelations() {
        // Given
        var unit = Unit.create("");
        Assignment assignment1 = Assignment.create("Assignment1", "unit123");
        Assignment assignment2 = Assignment.create("Assignment2", "unit456");
        MaterialRelation materialRelation1 = MaterialRelation.create("materialId", assignment1.getId(), MaterialRelation.RelationType.ASSIGNMENT);
        MaterialRelation materialRelation2 = MaterialRelation.create("materialId", assignment2.getId(), MaterialRelation.RelationType.ASSIGNMENT);

        when(assignmentRepository.findAllByUnitId(anyString())).thenReturn(Flux.just(assignment1, assignment2));
        when(materialRelationsRepository.findByAssignmentId(anyString())).thenReturn(Flux.just(materialRelation1, materialRelation2));

        // When & Then
        StepVerifier.create(assignmentService.findAllWithRelations(unit))
                .assertNext(assignment -> assertEquals("Assignment1", assignment.getName()))
                .assertNext(assignment -> assertEquals("Assignment2", assignment.getName()))
                .verifyComplete();
    }

    @Test
    public void testAttachAssignmentToNonExistentUnit() {
        String assignmentId = "assignmentId";
        String unitId = "unitId";
        Unit unit = Unit.create("");
        Assignment assignment = Assignment.create("", unit.getId());

        when(unitRepository.existsById(unitId)).thenReturn(Mono.just(false));
        when(assignmentRepository.findById(assignmentId)).thenReturn(Mono.just(assignment));

        StepVerifier.create(assignmentService.attachAssignmentToUnit(assignmentId, unitId))
                .expectError(UnitNotFoundException.class)
                .verify();
    }

    @Test
    public void testAttachAssignmentToAlreadyAttachedUnit() {
        Unit unit = Unit.create("");
        Assignment assignment = Assignment.create("", unit.getId());

        when(unitRepository.existsById(anyString())).thenReturn(Mono.just(true));
        when(assignmentRepository.findById(anyString())).thenReturn(Mono.just(assignment));

        StepVerifier.create(assignmentService.attachAssignmentToUnit(assignment.getId(), unit.getId()))
                .expectError(RelationAlreadyExistsException.class)
                .verify();
    }

    @Test
    public void testSuccessfulAttachAssignmentToUnit() {
        Unit unit = Unit.create("");
        Assignment assignment = Assignment.create("", null);

        when(unitRepository.existsById(anyString())).thenReturn(Mono.just(true));
        when(assignmentRepository.findById(anyString())).thenReturn(Mono.just(assignment));
        when(assignmentRepository.attachAssignmentToUnit(anyString(), anyString())).thenReturn(Mono.just(1));

        StepVerifier.create(assignmentService.attachAssignmentToUnit(assignment.getId(), unit.getId()))
                .assertNext(savedAssignment -> {
                    assertEquals(savedAssignment.getUnitId(), unit.getId());
                    verify(assignmentRepository).attachAssignmentToUnit(anyString(), anyString());
                })
                .verifyComplete();
    }

}
