package chaincue.tech.r2dbcbackend2.masters.unit_master.repositories;

import chaincue.tech.r2dbcbackend2.masters.unit_master.UnitTagRelation;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface UnitTagRelationRepository extends ReactiveCrudRepository<UnitTagRelation, String> {
    Flux<UnitTagRelation> findByUnitId(String unitId);
    Flux<UnitTagRelation> findByTagId(String tagId);

    Mono<Boolean> existsByUnitIdAndTagId(String unitId, String tagId);
}
