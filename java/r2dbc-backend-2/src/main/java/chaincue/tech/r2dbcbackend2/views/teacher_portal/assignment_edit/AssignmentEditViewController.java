package chaincue.tech.r2dbcbackend2.views.teacher_portal.assignment_edit;

import chaincue.tech.r2dbcbackend2.views.teacher_portal.assignments.DTOs.TeacherRequestBodyUpdateQueriesPriorities;
import chaincue.tech.r2dbcbackend2.views.teacher_portal.assignments.DTOs.TeacherSetFieldAssignmentRequestBody;
import chaincue.tech.r2dbcbackend2.views.teacher_portal.assignments.DTOs.TeacherUpdateAssignmentFieldDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("teacher-edit-assignment")
@AllArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class AssignmentEditViewController {

    @GetMapping("{assignmentId}")
    public Mono<AssignmentEditDTO> assignmentEditView(@RequestHeader("Authorization") String token, @PathVariable String assignmentId) {
        return Mono.empty();
    }

    @PutMapping("set-assignment-field")
    public Mono<AssignmentEditDTO> setAssignmentFields(@RequestHeader("Authorization") String token,
                                                       @RequestBody TeacherSetFieldAssignmentRequestBody teacherSetFieldAssignmentRequestBody) {
        return Mono.empty();
    }

    @PutMapping("add-query-field")
    public Mono<AssignmentEditDTO> addQueryField(@RequestHeader("Authorization") String token,
                                                 @RequestBody TeacherSetFieldAssignmentRequestBody teacherSetFieldAssignmentRequestBody) {
        return Mono.empty();
    }

    @PutMapping("add-query-option")
    public Mono<AssignmentEditDTO> addQueryOption(@RequestHeader("Authorization") String token,
                                                  @RequestBody TeacherSetFieldAssignmentRequestBody teacherSetFieldAssignmentRequestBody) {
        return Mono.empty();
    }

    @DeleteMapping("remove-query-field")
    public Mono<AssignmentEditDTO> removeQueryFields(@RequestHeader("Authorization") String token,
                                                     @RequestBody TeacherSetFieldAssignmentRequestBody teacherSetFieldAssignmentRequestBody) {
        return Mono.empty();
    }

    @PutMapping("update-query-field")
    public Mono<AssignmentEditDTO> updateQueryFields(@RequestHeader("Authorization") String token,
                                                     @RequestBody TeacherUpdateAssignmentFieldDTO teacherUpdateAssignmentFieldDTO) {
        return Mono.empty();
    }

    @PutMapping("update-query-option-field")
    public Mono<AssignmentEditDTO> updateOptionQueryFields(@RequestHeader("Authorization") String token,
                                                           @RequestBody TeacherUpdateAssignmentFieldDTO teacherUpdateAssignmentFieldDTO) {
        return Mono.empty();
    }

    @PostMapping("duplicate/{assignmentId}")
    public Mono<AssignmentEditDTO> duplicateAssignment(@RequestHeader("Authorization") String token, @PathVariable String assignmentId) {
        return Mono.empty();
    }

    @PutMapping("update-query-priorities")
    public Mono<AssignmentEditDTO> updateQueryPriorities(@RequestHeader("Authorization") String token, @RequestBody TeacherRequestBodyUpdateQueriesPriorities teacherRequestBodyUpdateQueriesPriorities) {
        return Mono.empty();
    }
}
