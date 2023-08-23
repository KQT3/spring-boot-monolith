package chaincue.tech.r2dbcbackend2.views.teacher_portal.assignment_edit;

public record AssignmentEditDTO(
        String id,
        String name,
        String assignmentType,
        Query[] queries,
        GitLabAssignmentType gitLabAssignmentType
) {
    public record Query(
            String id,
            String name,
            String kind,
            String answer,
            String query,
            int priority,
            Option[] options
    ) {
    }

    public record GitLabAssignmentType(
            String id,
            String name,
            String description
    ) {
    }

    public record Option(
            String id,
            String name
    ) {
    }

}
