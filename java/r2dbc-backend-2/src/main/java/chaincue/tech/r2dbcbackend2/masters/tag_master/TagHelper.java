package chaincue.tech.r2dbcbackend2.masters.tag_master;

import chaincue.tech.r2dbcbackend2.exeptions.MaterialNotFoundException;
import chaincue.tech.r2dbcbackend2.exeptions.TagNotFoundException;
import chaincue.tech.r2dbcbackend2.masters.material_master.MaterialRelation;
import chaincue.tech.r2dbcbackend2.masters.material_master.MaterialRelationsRepository;
import chaincue.tech.r2dbcbackend2.masters.material_master.MaterialRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class TagHelper {
    private final TagRepository tagRepository;
    private final MaterialRepository materialRepository;
    private final MaterialRelationsRepository materialRelationsRepository;


    @Transactional
    public Mono<Tag> save(String name) {
        var tag = Tag.create(name);
        return tagRepository.save(tag);
    }

    @Transactional
    public Mono<Tag> findByTagId(String tagId) {
        return tagRepository.findById(tagId)
                .switchIfEmpty(Mono.error(new TagNotFoundException(tagId)))
                .flatMap(this::findMaterialRelations);
    }

    private Mono<Tag> findMaterialRelations(Tag tag) {
        return materialRelationsRepository.findByTagId(tag.getId())
                .mapNotNull(MaterialRelation::getId)
                .collectList()
                .doOnNext(tag::setMaterialRelations)
                .thenReturn(tag);
    }

    @Transactional
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
