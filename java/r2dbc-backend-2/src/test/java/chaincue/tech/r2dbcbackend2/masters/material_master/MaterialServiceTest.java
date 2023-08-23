package chaincue.tech.r2dbcbackend2.masters.material_master;

import chaincue.tech.r2dbcbackend2.exeptions.MaterialNotFoundException;
import chaincue.tech.r2dbcbackend2.masters.material_master.repositories.MaterialRelationsRepository;
import chaincue.tech.r2dbcbackend2.masters.material_master.repositories.MaterialRepository;
import chaincue.tech.r2dbcbackend2.masters.tag_master.TagRepository;
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
class MaterialServiceTest {
    @InjectMocks
    MaterialService materialService;
    @Mock
    MaterialRepository materialRepository;
    @Mock
    UnitRepository unitRepository;
    @Mock
    TagRepository tagRepository;
    @Mock
    MaterialRelationsRepository materialRelationsRepository;

    @Test
    void save_ShouldSaveMaterialWithGivenName() {
        // Given
        String givenName = "TestMaterial";
        Material.MaterialType materialType = Material.MaterialType.PDF;
        String uri = "http://example.com/material";
        Material mockMaterial = Material.create(givenName, materialType, uri);
        when(materialRepository.save(any(Material.class))).thenReturn(Mono.just(mockMaterial));

        // When & Then
        StepVerifier.create(materialService.save(givenName, materialType, uri))
                .assertNext(material -> {
                    assertEquals(givenName, material.getName());
                    assertEquals(materialType, material.getMaterialType());
                    assertEquals(uri, material.getUri());
                    verify(materialRepository).save(any());
                })
                .verifyComplete();
    }

    @Test
    void findMaterialById_ShouldReturnMaterial_WhenMaterialIdIsValid() {
        // Given
        String validMaterialId = "12345";
        Material mockMaterial = Material.create("givenName", Material.MaterialType.PDF, "http://example.com/material");
        when(materialRepository.findById(anyString())).thenReturn(Mono.just(mockMaterial));

        // When & Then
        StepVerifier.create(materialService.findMaterialById(validMaterialId))
                .assertNext(material -> assertEquals(mockMaterial, material))
                .verifyComplete();
    }


    @Test
    void findMaterialByIdWithRelations_ShouldReturnMaterialWithRelations_WhenMaterialIdIsValid() {
        // Given
        String validMaterialId = "12345";
        Material mockMaterial = Material.create("givenName", Material.MaterialType.PDF, "http://example.com/material");
        MaterialRelation materialRelation = MaterialRelation.create(mockMaterial.getId(), "", MaterialRelation.RelationType.COURSE);

        when(materialRepository.findById(validMaterialId)).thenReturn(Mono.just(mockMaterial));
        when(materialRelationsRepository.findByMaterialId(anyString())).thenReturn(Flux.just(materialRelation));

        // When & Then
        StepVerifier.create(materialService.findMaterialByIdWithRelations(validMaterialId))
                .assertNext(material -> assertEquals(mockMaterial, material))
                .verifyComplete();
    }

    @Test
    void findMaterialByIdWithRelations_ShouldThrowMaterialNotFoundException_WhenMaterialIdIsInvalid() {
        // Given
        String invalidMaterialId = "54321";
        when(materialRepository.findById(invalidMaterialId)).thenReturn(Mono.empty());

        // When & Then
        StepVerifier.create(materialService.findMaterialByIdWithRelations(invalidMaterialId))
                .expectError(MaterialNotFoundException.class)
                .verify();
    }

    @Test
    void findByNameContainingIgnoreCase() {
        // Given
        var material1 = Material.create("material1", Material.MaterialType.PNG, "");
        var material2 = Material.create("material2", Material.MaterialType.PNG, "");
        String[] searchValues = {"material1", "material2"};

        when(materialRepository.findAll()).thenReturn(Flux.just(material1, material2));
        when(materialRelationsRepository.findByMaterialId(anyString())).thenReturn(Flux.empty());

        // When & Then
        StepVerifier.create(materialService.findByNameContainingIgnoreCase(searchValues))
                .expectNext(material1)
                .expectNext(material2)
                .verifyComplete();
    }

}
