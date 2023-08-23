package chaincue.tech.r2dbcbackend2.views.teacher_view.assignments.DTOs;

public record TeacherUpdateAssignmentFieldDTO(
        String assignmentId,
        String queryId,
        String fieldType,
        String newValue
) {
}
