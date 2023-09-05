package chaincue.tech.r2dbcbackend2.masters.course_master;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

@RequiredArgsConstructor
@Component
public class CourseHelper {
    private final CourseService courseService;

    public <P> Function<P, Mono<P>> updateParamWithCourses(BiConsumer<P, List<Course>> setCourses) {
        return param -> courseService.findAllWithRelations()
                .collectList()
                .doOnNext(courses -> setCourses.accept(param, courses))
                .thenReturn(param);
    }

    public <P> Function<P, Mono<P>> updateParamWithCourse(Function<P, Course> getCourse, BiConsumer<P, Course> setCourse) {
        return param -> courseService.findCourseById(getCourse.apply(param).getId())
                .doOnNext(courses -> setCourse.accept(param, courses))
                .thenReturn(param);
    }
}
