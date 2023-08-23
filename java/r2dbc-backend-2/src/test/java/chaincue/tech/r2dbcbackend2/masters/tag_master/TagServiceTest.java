package chaincue.tech.r2dbcbackend2.masters.tag_master;

import chaincue.tech.r2dbcbackend2.exeptions.TagNotFoundException;
import chaincue.tech.r2dbcbackend2.masters.material_master.MaterialRelation;
import chaincue.tech.r2dbcbackend2.masters.material_master.repositories.MaterialRelationsRepository;
import chaincue.tech.r2dbcbackend2.masters.material_master.repositories.MaterialRepository;
import chaincue.tech.r2dbcbackend2.masters.unit_master.Unit;
import chaincue.tech.r2dbcbackend2.masters.unit_master.UnitTagRelation;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TagServiceTest {
    @InjectMocks
    TagService tagService;
    @Mock
    TagRepository tagRepository;
    @Mock
    MaterialRepository materialRepository;
    @Mock
    MaterialRelationsRepository materialRelationsRepository;
    @Mock
    UnitTagRelationRepository unitTagRelationRepository;

    @Test
    void saveSuccess() {
        // Given
        var expectedData = Tag.create("tag");
        when(tagRepository.save(any())).thenReturn(Mono.just(expectedData));

        // When & Then
        StepVerifier.create(tagService.save("tag"))
                .assertNext(result -> {
                    assertEquals(expectedData, result);
                    verify(tagRepository).save(any());
                })
                .verifyComplete();
    }

    @Test
    void findByTagId_ShouldReturnTag_WhenTagIsFound() {
        // Given
        String tagId = "12345";
        Tag expectedTagWithRelations = Tag.create("");
        var materialRelationMock = mock(MaterialRelation.class);

        when(tagRepository.findById(anyString())).thenReturn(Mono.just(expectedTagWithRelations));
        when(materialRelationsRepository.findByTagId(any())).thenReturn(Flux.just(materialRelationMock));

        // When & Then
        StepVerifier.create(tagService.findByTagId(tagId))
                .assertNext(tag -> {
                    assertEquals(expectedTagWithRelations, tag);
                })
                .verifyComplete();
    }

    @Test
    void findByTagId_ShouldThrowException_WhenTagIsNotFound() {
        // Given
        String tagId = "12345";

        when(tagRepository.findById(tagId)).thenReturn(Mono.empty());

        // When & Then
        StepVerifier.create(tagService.findByTagId(tagId))
                .expectErrorMatches(throwable -> throwable instanceof TagNotFoundException &&
                        throwable.getMessage().contains(tagId))
                .verify();
    }

    @Test
    void findAllWithRelations_ShouldReturnAllTagsWithRelations() {
        // Given
        Tag tag1 = Tag.create("");
        Tag tag2 = Tag.create("");
        MaterialRelation materialRelationMock1 = MaterialRelation.create("", tag1.getId(), MaterialRelation.RelationType.TAG);
        MaterialRelation materialRelationMock2 = MaterialRelation.create("", tag2.getId(), MaterialRelation.RelationType.TAG);

        when(tagRepository.findAll()).thenReturn(Flux.just(tag1, tag2));
        when(materialRelationsRepository.findByTagId(anyString())).thenReturn(Flux.just(materialRelationMock1, materialRelationMock2));

        // When & Then
        StepVerifier.create(tagService.findAllWithRelations())
                .assertNext(tag -> {
                    assertEquals(materialRelationMock1.getTagId(), tag.getId());
                })
                .assertNext(tag -> assertEquals(materialRelationMock2.getTagId(), tag.getId()))
                .verifyComplete();
    }

    @Test
    void findAllWithRelationsAndUnit_ShouldReturnAllTagsWithRelations() {
        // Given
        Tag tag1 = Tag.create("");
        MaterialRelation materialRelationMock1 = MaterialRelation.create("", tag1.getId(), MaterialRelation.RelationType.TAG);
        Unit unit = Unit.create("");
        var unitTagRelation = UnitTagRelation.create(unit.getId(), tag1.getId());

        when(unitTagRelationRepository.findByUnitId(anyString())).thenReturn(Flux.just(unitTagRelation));
        when(tagRepository.findById(anyString())).thenReturn(Mono.just(tag1));
        when(materialRelationsRepository.findByTagId(anyString())).thenReturn(Flux.just(materialRelationMock1));

        // When & Then
        StepVerifier.create(tagService.findAllWithRelations(unit))
                .assertNext(tag -> assertEquals(materialRelationMock1.getTagId(), tag.getId()))
                .verifyComplete();
    }
}
