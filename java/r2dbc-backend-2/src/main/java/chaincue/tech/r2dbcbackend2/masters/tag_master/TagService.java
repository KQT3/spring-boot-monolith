package chaincue.tech.r2dbcbackend2.masters.tag_master;

import chaincue.tech.r2dbcbackend2.exeptions.MaterialNotFoundException;
import chaincue.tech.r2dbcbackend2.exeptions.TagNotFoundException;
import chaincue.tech.r2dbcbackend2.masters.material_master.MaterialRelation;
import chaincue.tech.r2dbcbackend2.masters.material_master.repositories.MaterialRelationsRepository;
import chaincue.tech.r2dbcbackend2.masters.material_master.repositories.MaterialRepository;
import chaincue.tech.r2dbcbackend2.masters.unit_master.Unit;
import chaincue.tech.r2dbcbackend2.masters.unit_master.repositories.UnitTagRelationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Service
@AllArgsConstructor
public class TagService implements TagContract {
    private final TagRepository tagRepository;
    private final MaterialRepository materialRepository;
    private final MaterialRelationsRepository materialRelationsRepository;
    private final UnitTagRelationRepository unitTagRelationRepository;


    @Transactional
    @Override
    public Mono<Tag> save(String name) {
        var tag = Tag.create(Objects.equals(name, "") ? "default" : name);
        return tagRepository.save(tag);
    }

    @Transactional
    @Override
    public Mono<Tag> findByTagId(String tagId) {
        return tagRepository.findById(tagId)
                .switchIfEmpty(Mono.error(new TagNotFoundException(tagId)))
                .flatMap(this::fetchMaterialRelationForTags);
    }

    @Override
    public Flux<Tag> findAllWithRelations() {
        return tagRepository.findAll().flatMap(this::fetchMaterialRelationForTags);
    }

    @Override
    public Flux<Tag> findAllWithRelations(Unit unit) {
        return unitTagRelationRepository.findByUnitId(unit.getId())
                .flatMap(relation -> tagRepository.findById(relation.getTagId())
                        .flatMap(this::fetchMaterialRelationForTags));
    }

    private Mono<Tag> fetchMaterialRelationForTags(Tag tag) {
        return materialRelationsRepository.findByTagId(tag.getId())
                .mapNotNull(MaterialRelation::getId)
                .collectList()
                .doOnNext(tag::setMaterialRelations)
                .thenReturn(tag);
    }

    @Transactional
    @Override
    public Mono<MaterialRelation> addTagToMaterial(String tagId, String materialId) {
        var materialRelation = MaterialRelation.create(materialId, tagId, MaterialRelation.RelationType.TAG);
        return Mono.zip(tagRepository.existsById(tagId), materialRepository.existsById(materialId),
                        (courseExists, materialExists) -> {
                            if (!courseExists) throw new TagNotFoundException(tagId);
                            if (!materialExists) throw new MaterialNotFoundException(materialId);
                            return materialRelation;
                        })
                .flatMap(materialRelationsRepository::save);
    }

}
