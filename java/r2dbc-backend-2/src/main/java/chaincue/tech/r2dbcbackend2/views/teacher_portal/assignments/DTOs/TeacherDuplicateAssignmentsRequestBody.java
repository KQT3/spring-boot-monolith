package chaincue.tech.r2dbcbackend2.views.teacher_portal.assignments.DTOs;

public record TeacherDuplicateAssignmentsRequestBody(Assignment[] duplicatedAssignments) {
    public record Assignment(
            String id,
            String assignmentName,
            String courseName,
            String unitName,
            String assignmentType
    ) {
    }
}
