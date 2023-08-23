package chaincue.tech.r2dbcbackend2.masters.unit_master;

import chaincue.tech.r2dbcbackend2.exeptions.RelationAlreadyExistsException;
import chaincue.tech.r2dbcbackend2.exeptions.RelationNotFoundException;
import chaincue.tech.r2dbcbackend2.masters.assignment_master.repositories.AssignmentRepository;
import chaincue.tech.r2dbcbackend2.masters.course_master.Course;
import chaincue.tech.r2dbcbackend2.masters.course_master.repositories.CourseRepository;
import chaincue.tech.r2dbcbackend2.masters.material_master.MaterialRelation;
import chaincue.tech.r2dbcbackend2.masters.material_master.repositories.MaterialRelationsRepository;
import chaincue.tech.r2dbcbackend2.masters.tag_master.TagRepository;
import chaincue.tech.r2dbcbackend2.masters.unit_master.repositories.UnitCourseRelationRepository;
import chaincue.tech.r2dbcbackend2.masters.unit_master.repositories.UnitRepository;
import chaincue.tech.r2dbcbackend2.masters.unit_master.repositories.UnitTagRelationRepository;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UnitServiceTest {
    @InjectMocks
    UnitService unitService;
    @Mock
    UnitRepository unitRepository;
    @Mock
    CourseRepository courseRepository;
    @Mock
    TagRepository tagRepository;
    @Mock
    UnitCourseRelationRepository unitCourseRelationRepository;
    @Mock
    MaterialRelationsRepository materialRelationsRepository;
    @Mock
    UnitTagRelationRepository unitTagRelationRepository;
    @Mock
    AssignmentRepository assignmentRepository;

    @Test
    void shouldSaveUnitSuccessfully() {
        // Given
        String unitName = "unit";
        Unit expectedUnit = Unit.create(unitName);

        when(unitRepository.save(any())).thenReturn(Mono.just(expectedUnit));

        // When & Then
        StepVerifier.create(unitService.save(unitName))
                .expectNextMatches(savedUnit -> savedUnit.equals(expectedUnit))
                .verifyComplete();

        verify(unitRepository, times(1)).save(any());
    }

    @Test
    void addUnitToCourse_relationAlreadyExists() {
        // Given
        String unitId = "unitId";
        String courseId = "courseId";
        when(unitCourseRelationRepository.existsByUnitIdAndCourseId(unitId, courseId)).thenReturn(Mono.just(true));

        // When & Then
        StepVerifier.create(unitService.addUnitToCourse(unitId, courseId))
                .expectErrorMatches(throwable -> throwable instanceof RelationAlreadyExistsException)
                .verify();
    }

    @Test
    void addUnitToCourse_successfullyAddsRelation() {
        // Given
        String unitId = "unitId";
        String courseId = "courseId";
        UnitCourseRelation relation = UnitCourseRelation.create(unitId, courseId);

        when(unitCourseRelationRepository.existsByUnitIdAndCourseId(unitId, courseId)).thenReturn(Mono.just(false));
        when(unitCourseRelationRepository.save(any())).thenReturn(Mono.just(relation));

        // When & Then
        StepVerifier.create(unitService.addUnitToCourse(unitId, courseId))
                .expectNext(relation)
                .verifyComplete();
    }

    @Test
    void addUnitToMaterial_successfullyAddsRelation() {
        // Given
        String unitId = "unitId";
        String materialId = "materialId";
        var relation = MaterialRelation.create(materialId, unitId, MaterialRelation.RelationType.UNIT);

        when(materialRelationsRepository.existsByMaterialIdAndUnitId(materialId, unitId)).thenReturn(Mono.just(false));
        when(materialRelationsRepository.save(any())).thenReturn(Mono.just(relation));

        // When & Then
        StepVerifier.create(unitService.addUnitToMaterial(unitId, materialId))
                .expectNext(relation)
                .verifyComplete();
    }

    @Test
    void addUnitToMaterial_relationAlreadyExists() {
        // Given
        String unitId = "unitId";
        String materialId = "materialId";

        when(materialRelationsRepository.existsByMaterialIdAndUnitId(materialId, unitId)).thenReturn(Mono.just(true));

        // When & Then
        StepVerifier.create(unitService.addUnitToMaterial(unitId, materialId))
                .expectErrorMatches(throwable -> throwable instanceof RelationAlreadyExistsException)
                .verify();
    }

    @Test
    void addUnitToTag_successfullyAddsRelation() {
        // Given
        String unitId = "unitId";
        String tagId = "tagId";
        UnitTagRelation relation = UnitTagRelation.create(unitId, tagId);

        when(unitTagRelationRepository.existsByUnitIdAndTagId(unitId, tagId)).thenReturn(Mono.just(false));
        when(unitTagRelationRepository.save(any())).thenReturn(Mono.just(relation));

        // When & Then
        StepVerifier.create(unitService.addUnitToTag(unitId, tagId))
                .expectNext(relation)
                .verifyComplete();
    }

    @Test
    void addUnitToTag_relationAlreadyExists() {
        // Given
        String unitId = "unitId";
        String tagId = "tagId";

        when(unitTagRelationRepository.existsByUnitIdAndTagId(unitId, tagId)).thenReturn(Mono.just(true));

        // When & Then
        StepVerifier.create(unitService.addUnitToTag(unitId, tagId))
                .expectErrorMatches(throwable -> throwable instanceof RelationAlreadyExistsException)
                .verify();
    }

    @Test
    void findUnitByIdWithRelationsSuccess() {
        // Given
        String unitId = "unitId";
        Unit unit = Unit.create("unit");

        when(unitRepository.findById(anyString())).thenReturn(Mono.just(unit));
        when(unitCourseRelationRepository.findByUnitId(anyString())).thenReturn(Flux.empty());
        when(materialRelationsRepository.findByUnitId(anyString())).thenReturn(Flux.empty());
        when(unitTagRelationRepository.findByUnitId(anyString())).thenReturn(Flux.empty());
        when(courseRepository.findAllById(anyList())).thenReturn(Flux.empty());
        when(tagRepository.findAllById(anyList())).thenReturn(Flux.empty());
        when(assignmentRepository.findAllByUnitId(anyString())).thenReturn(Flux.empty());

        // When & Then
        StepVerifier.create(unitService.findUnitByIdWithRelations(unitId))
                .expectNext(unit)
                .verifyComplete();
    }


    @Test
    void findAllWithRelationsSuccess() {
        // Given
        Unit unit = Unit.create("unit");

        when(unitRepository.findAll()).thenReturn(Flux.just(unit));
        when(unitCourseRelationRepository.findByUnitId(anyString())).thenReturn(Flux.empty());
        when(materialRelationsRepository.findByUnitId(anyString())).thenReturn(Flux.empty());
        when(unitTagRelationRepository.findByUnitId(anyString())).thenReturn(Flux.empty());
        when(courseRepository.findAllById(anyList())).thenReturn(Flux.empty());
        when(tagRepository.findAllById(anyList())).thenReturn(Flux.empty());
        when(assignmentRepository.findAllByUnitId(anyString())).thenReturn(Flux.empty());

        // When & Then
        StepVerifier.create(unitService.findAllWithRelations())
                .expectNext(unit)
                .verifyComplete();
    }

    @Test
    void findAllWithRelationsWithCourseSuccess() {
        // Given
        Unit unit = Unit.create("unit");
        Course course = Course.create("unit");
        UnitCourseRelation unitCourseRelation = UnitCourseRelation.create(unit.getId(), course.getId());

        when(unitCourseRelationRepository.findByCourseId(anyString())).thenReturn(Flux.just(unitCourseRelation));
        when(unitRepository.findById(anyString())).thenReturn(Mono.just(unit));
        when(unitCourseRelationRepository.findByUnitId(anyString())).thenReturn(Flux.empty());
        when(materialRelationsRepository.findByUnitId(anyString())).thenReturn(Flux.empty());
        when(unitTagRelationRepository.findByUnitId(anyString())).thenReturn(Flux.empty());
        when(courseRepository.findAllById(anyList())).thenReturn(Flux.empty());
        when(tagRepository.findAllById(anyList())).thenReturn(Flux.empty());
        when(assignmentRepository.findAllByUnitId(anyString())).thenReturn(Flux.empty());

        // When & Then
        StepVerifier.create(unitService.findAllWithRelations(course))
                .expectNext(unit)
                .verifyComplete();
    }

    @Test
    void removeUnitFromCourse_ExistingRelation_RelationRemovedSuccessfully() {
        // Given
        String unitId = "unitId";
        String courseId = "courseId";
        UnitCourseRelation mockRelation = UnitCourseRelation.create(unitId, courseId);
        mockRelation.setId("relationId");

        when(unitCourseRelationRepository.findByUnitIdAndCourseId(unitId, courseId)).thenReturn(Mono.just(mockRelation));
        when(unitCourseRelationRepository.deleteById(mockRelation.getId())).thenReturn(Mono.empty());

        // When & Then
        StepVerifier.create(unitService.removeUnitFromCourse(unitId, courseId))
                .expectNext(mockRelation)
                .verifyComplete();
        verify(unitCourseRelationRepository).deleteById(mockRelation.getId());
    }

    @Test
    void removeUnitFromCourse_NonExistingRelation_ThrowsRelationNotFoundException() {
        // Given
        String unitId = "unitId";
        String courseId = "courseId";

        when(unitCourseRelationRepository.findByUnitIdAndCourseId(unitId, courseId))
                .thenReturn(Mono.empty());

        // When & Then
        StepVerifier.create(unitService.removeUnitFromCourse(unitId, courseId))
                .expectErrorMatches(throwable -> throwable instanceof RelationNotFoundException)
                .verify();
    }

    @Test
    void findByNameContainingIgnoreCase() {
        // Given
        Unit mockUnit1 = Unit.create("SampleUnit1");
        Unit mockUnit2 = Unit.create("SampleUnit2");
        String[] searchValues = {"sample"};

        when(unitRepository.findAll()).thenReturn(Flux.just(mockUnit1, mockUnit2));
        when(unitCourseRelationRepository.findByUnitId(anyString())).thenReturn(Flux.empty());
        when(materialRelationsRepository.findByUnitId(anyString())).thenReturn(Flux.empty());
        when(unitTagRelationRepository.findByUnitId(anyString())).thenReturn(Flux.empty());
        when(courseRepository.findAllById(anyList())).thenReturn(Flux.empty());
        when(tagRepository.findAllById(anyList())).thenReturn(Flux.empty());

        // When & Then
        StepVerifier.create(unitService.findByNameContainingIgnoreCase(searchValues))
                .expectNext(mockUnit1)
                .expectNext(mockUnit2)
                .verifyComplete();
    }

    @Test
    public void updateUnitField_Unit_ShouldUpdateTitle() {
        // Given
        String unitId = "testCourseId";
        Unit mockUnit = Unit.create("unit");
        String expectedNewValue = "New Title";

        when(unitRepository.findById(unitId)).thenReturn(Mono.just(mockUnit));
        when(unitRepository.updateUnitName(unitId, expectedNewValue)).thenReturn(Mono.empty());

        // When & Then
        StepVerifier.create(unitService.updateUnitField(unitId, expectedNewValue, Unit.ChangeFieldName.TITLE))
                .assertNext(unit -> assertEquals(expectedNewValue, unit.getName()))
                .verifyComplete();
    }
    @Test
    public void updateCourseField_Unit_ShouldUpdateDescription() {
        // Given
        String unitId = "testCourseId";
        Unit mockUnit = Unit.create("unit");
        String expectedNewValue = "New Title";

        when(unitRepository.findById(unitId)).thenReturn(Mono.just(mockUnit));
        when(unitRepository.updateUnitDescription(unitId, expectedNewValue)).thenReturn(Mono.empty());

        // When & Then
        StepVerifier.create(unitService.updateUnitField(unitId, expectedNewValue, Unit.ChangeFieldName.DESCRIPTION))
                .assertNext(unit -> assertEquals(expectedNewValue, unit.getDescription()))
                .verifyComplete();
    }

}
