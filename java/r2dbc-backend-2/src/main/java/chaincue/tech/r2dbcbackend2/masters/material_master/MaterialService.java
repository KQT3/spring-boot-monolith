package chaincue.tech.r2dbcbackend2.masters.material_master;

import chaincue.tech.r2dbcbackend2.exeptions.MaterialNotFoundException;
import chaincue.tech.r2dbcbackend2.masters.course_master.Course;
import chaincue.tech.r2dbcbackend2.masters.material_master.repositories.MaterialRelationsRepository;
import chaincue.tech.r2dbcbackend2.masters.material_master.repositories.MaterialRepository;
import chaincue.tech.r2dbcbackend2.masters.tag_master.TagRepository;
import chaincue.tech.r2dbcbackend2.masters.unit_master.Unit;
import chaincue.tech.r2dbcbackend2.masters.unit_master.repositories.UnitRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Objects;

@Service
@AllArgsConstructor
public class MaterialService implements MaterialContract {
    private final MaterialRepository materialRepository;
    private final UnitRepository unitRepository;
    private final TagRepository tagRepository;
    private final MaterialRelationsRepository materialRelationsRepository;

    @Transactional
    @Override
    public Mono<Material> save(String name, Material.MaterialType materialType, String uri) {
        var material = Material.create(Objects.equals(name, "") ? "default" : name, materialType, uri);
        return materialRepository.save(material);
    }

    @Transactional
    @Override
    public Mono<Material> findMaterialById(String materialId) {
        return materialRepository.findById(materialId);
    }

    @Transactional
    @Override
    public Mono<Material> findMaterialByIdWithRelations(String materialId) {
        return materialRepository.findById(materialId)
                .switchIfEmpty(Mono.error(new MaterialNotFoundException(materialId)))
                .flatMap(this::fetchMaterialRelationForMaterial);
    }

    @Override
    public Flux<Material> findAllWithRelations() {
        return materialRepository.findAll().flatMap(this::fetchMaterialRelationForMaterial);
    }

    @Override
    public Flux<Material> findAllWithRelations(Course course) {
        if (course.getMaterialRelations() != null){
            return materialRelationsRepository.findAllById(course.getMaterialRelations())
                    .flatMap(relation -> materialRepository.findById(relation.getMaterialId()))
                    .flatMap(this::fetchMaterialRelationForMaterial);
        }
        return Flux.empty();
    }

    @Override
    public Flux<Material> findAllWithRelations(Unit unit) {
        if (unit.getMaterialRelations() != null){
            return materialRelationsRepository.findAllById(unit.getMaterialRelations())
                    .flatMap(relation -> materialRepository.findById(relation.getMaterialId()))
                    .flatMap(this::fetchMaterialRelationForMaterial);
        }
        return Flux.empty();
    }

    @Override
    public Flux<Material> findByNameContainingIgnoreCase(String[] searchValues) {
        return materialRepository.findAll()
                .filter(material -> {
                    if (searchValues == null || searchValues.length == 0) return true;
                    return Arrays.stream(searchValues).anyMatch(val -> material.getName().toLowerCase().contains(val.toLowerCase()));
                })
                .flatMap(this::fetchMaterialRelationForMaterial);
    }

    private Mono<Material> fetchMaterialRelationForMaterial(Material material) {
        return materialRelationsRepository.findByMaterialId(material.getId())
                .mapNotNull(MaterialRelation::getId)
                .collectList()
                .doOnNext(material::setMaterialRelations)
                .thenReturn(material);
    }

}
