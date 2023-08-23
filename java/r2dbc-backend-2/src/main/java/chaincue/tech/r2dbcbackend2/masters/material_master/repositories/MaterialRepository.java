package chaincue.tech.r2dbcbackend2.masters.material_master.repositories;

import chaincue.tech.r2dbcbackend2.masters.material_master.Material;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MaterialRepository extends ReactiveCrudRepository<Material, String> {
}
