package chaincue.tech.r2dbcbackend2.views.teacher_portal.assignments.DTOs;

public record TeacherUpdateAssignmentFieldDTO(
        String assignmentId,
        String queryId,
        String fieldType,
        String newValue
) {
}
