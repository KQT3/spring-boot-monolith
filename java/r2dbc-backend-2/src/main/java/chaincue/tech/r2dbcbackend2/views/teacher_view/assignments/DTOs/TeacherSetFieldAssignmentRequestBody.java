package chaincue.tech.r2dbcbackend2.views.teacher_view.assignments.DTOs;

public record TeacherSetFieldAssignmentRequestBody(
        String assignmentId,
        String assignmentName,
        String description,
        String assignmentTypes,
        String changeFieldType,
        Query query
) {
    public record Query(
            String id,
            String kind,
            String answer,
            String query,
            Option[] options
    ) {
    }

    public record Option(
            String id,
            String name
    ) {
    }

    public record GitLabAssignmentType(
            String id,
            String name,
            String description
    ) {
    }

}
