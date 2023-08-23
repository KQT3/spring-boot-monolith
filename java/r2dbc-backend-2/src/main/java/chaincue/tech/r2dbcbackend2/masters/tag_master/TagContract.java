package chaincue.tech.r2dbcbackend2.masters.tag_master;

import chaincue.tech.r2dbcbackend2.masters.material_master.MaterialRelation;
import chaincue.tech.r2dbcbackend2.masters.unit_master.Unit;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TagContract {
    Mono<Tag> save(String name);

    Mono<Tag> findByTagId(String tagId);

    Flux<Tag> findAllWithRelations();

    Flux<Tag> findAllWithRelations(Unit unit);

    Mono<MaterialRelation> addTagToMaterial(String tagId, String materialId);

}
