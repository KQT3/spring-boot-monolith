package chaincue.tech.r2dbcbackend2.masters.tag_master;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends ReactiveCrudRepository<Tag, String> {
}
