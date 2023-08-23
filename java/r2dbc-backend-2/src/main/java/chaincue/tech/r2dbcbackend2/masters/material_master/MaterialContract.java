package chaincue.tech.r2dbcbackend2.masters.material_master;

import chaincue.tech.r2dbcbackend2.masters.course_master.Course;
import chaincue.tech.r2dbcbackend2.masters.unit_master.Unit;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface MaterialContract {
    Mono<Material> save(String name, Material.MaterialType materialType, String uri);

    Mono<Material> findMaterialById(String materialId);

    Mono<Material> findMaterialByIdWithRelations(String materialId);

    Flux<Material> findAllWithRelations();

    Flux<Material> findAllWithRelations(Course course);

    Flux<Material> findAllWithRelations(Unit unit);

    Flux<Material> findByNameContainingIgnoreCase(String[] searchValues);
}
