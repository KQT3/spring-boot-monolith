package chaincue.tech.r2dbcbackend2.views.teacher_portal.assignments.DTOs;

public record TeacherRequestBodyUpdateQueriesPriorities(
        String assignmentId,
        Query[] queries
) {

    public record Query(
            String queryId,
            int priority
    ) {
    }
}
