package chaincue.tech.r2dbcbackend2.masters.course_master.repositories;

import chaincue.tech.r2dbcbackend2.masters.course_master.Course;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

@Repository
public interface CourseRepository extends ReactiveCrudRepository<Course, String> {
    @Modifying
    @Query("UPDATE courses SET name = :name WHERE id = :id")
    Mono<Integer> updateCourseName(String id, String name);

    @Modifying
    @Query("UPDATE courses SET description = :description WHERE id = :id")
    Mono<Integer> updateCourseDescription(String id, String description);

    @Modifying
    @Query("UPDATE courses SET start_date = :startDate WHERE id = :id")
    Mono<Integer> updateCourseStartDate(String id, LocalDate startDate);

    @Modifying
    @Query("UPDATE courses SET end_date = :endDate WHERE id = :id")
    Mono<Integer> updateCourseEndDate(String id, LocalDate endDate);

    @Modifying
    @Query("UPDATE courses SET status = :status WHERE id = :id")
    Mono<Integer> updateCourseStatus(String id, Course.Status status);
}
