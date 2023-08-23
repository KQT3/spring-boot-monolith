package chaincue.tech.r2dbcbackend2.views.teacher_view.units;

import chaincue.tech.r2dbcbackend2.masters.course_master.Course;
import chaincue.tech.r2dbcbackend2.masters.tag_master.Tag;
import chaincue.tech.r2dbcbackend2.masters.teacher_master.Teacher;
import chaincue.tech.r2dbcbackend2.masters.teacher_master.TeacherService;
import chaincue.tech.r2dbcbackend2.masters.unit_master.Unit;
import chaincue.tech.r2dbcbackend2.masters.unit_master.UnitService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static chaincue.tech.r2dbcbackend2.utilities.JWTDecoderUtil.getSubIdFromToken;


@RestController
@RequestMapping("teacher-units")
@RequiredArgsConstructor
@Slf4j
public class TeacherUnitsViewController {
    private final TeacherService teacherService;
    private final UnitService unitService;

    @GetMapping
    public Mono<TeacherUnitsViewDTO> unitsView(@RequestHeader("Authorization") String token) {
        return teacherService.getOrCreateTeacher(getSubIdFromToken(token))
                .map(Param::new)
                .flatMap(this::setAllUnits)
                .map(TeacherUnitsViewController::toDTO);
    }

    private Mono<Param> setAllUnits(Param param) {
        return unitService.findAllWithRelations()
                .collectList()
                .doOnNext(param::setUnits)
                .thenReturn(param);
    }

    @PostMapping("search")
    public Flux<TeacherUnitsViewDTO.Unit> searchUnitView(@RequestHeader("Authorization") String token, @RequestBody TeacherFilterUnitsDTO searchValues) {
        log.info("searchValues: {}", searchValues);
        return Flux.from(teacherService.getOrCreateTeacher(getSubIdFromToken(token))
                        .map(Param::new)
                        .flatMap(param -> filterUnitsBySearchValues(searchValues, param))
                        .map(param -> Flux.fromIterable(param.getUnits())
                                .sort(Comparator.comparing(Unit::getName))
                                .map(TeacherUnitsViewController::toDTO)))
                .flatMap(courseFlux -> courseFlux);
    }

    private Mono<Param> filterUnitsBySearchValues(TeacherFilterUnitsDTO searchValues, Param param) {
        return unitService.findAllWithRelations()
                .filter(unit -> {
                    if (searchValues.searchValues().length > 0) {
                        boolean nameMatched = Arrays.stream(searchValues.searchValues())
                                .anyMatch(s -> unit.getName().toLowerCase().contains(s.toLowerCase()));
                        boolean tagMatches = unit.getTags().stream()
                                .anyMatch(tag -> Arrays.stream(searchValues.searchValues())
                                        .anyMatch(s -> tag.getName().toLowerCase().contains(s.toLowerCase())));
                        return nameMatched | tagMatches;
                    }
                    return true;
                })
                .collectList()
                .doOnNext(param::setUnits)
                .thenReturn(param);
    }

    @PostMapping("create-unit/{unitName}")
    public Mono<String> createUnit(@RequestHeader("Authorization") String token, @PathVariable String unitName) {
        return teacherService.getOrCreateTeacher(getSubIdFromToken(token))
                .map(Param::new)
                .flatMap(param -> unitService.save(unitName).mapNotNull(Unit::getId));
    }

    public static TeacherUnitsViewDTO toDTO(Param param) {
        return new TeacherUnitsViewDTO(
                param.getTeacher().getId(),
                param.getTeacher().getName(),
                param.getUnits().stream()
                        .sorted(Comparator.comparing(Unit::getName))
                        .map(TeacherUnitsViewController::toDTO)
                        .toArray(TeacherUnitsViewDTO.Unit[]::new));
    }

    public static TeacherUnitsViewDTO.Unit toDTO(Unit unit) {
        return new TeacherUnitsViewDTO.Unit(
                unit.getId(),
                unit.getName(),
                unit.getCourses().stream().map(TeacherUnitsViewController::toDTO)
                        .toArray(TeacherUnitsViewDTO.Course[]::new),
                unit.getTags().stream()
                        .map(TeacherUnitsViewController::toDTO)
                        .toArray(TeacherUnitsViewDTO.Tag[]::new)
        );
    }

    public static TeacherUnitsViewDTO.Course toDTO(Course course) {
        return new TeacherUnitsViewDTO.Course(course.getId(), course.getName());
    }

    public static TeacherUnitsViewDTO.Tag toDTO(Tag tag) {
        return new TeacherUnitsViewDTO.Tag(tag.getId(), tag.getName());
    }

    @Data
    private static class Param {
        Teacher teacher;
        List<Unit> units;

        public Param(Teacher teacher) {
            this.teacher = teacher;
        }
    }
}
