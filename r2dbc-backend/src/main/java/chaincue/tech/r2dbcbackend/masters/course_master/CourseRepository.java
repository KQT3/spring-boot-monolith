package chaincue.tech.r2dbcbackend.masters.course_master;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public interface CourseRepository {
    Flux<Course> findAll();

    Mono<Course> findById(String id);

    Mono<Course> findByName(String name);

    Mono<Course> save(Course department);

    Mono<Void> delete(Course department);
}
