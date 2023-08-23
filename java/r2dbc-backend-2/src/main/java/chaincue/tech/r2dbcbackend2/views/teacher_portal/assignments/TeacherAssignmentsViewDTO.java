package chaincue.tech.r2dbcbackend2.views.teacher_portal.assignments;

public record TeacherAssignmentsViewDTO(
        String id,
        String name,
        Course[] courses,
        String[] assignmentTypes,
        Assignment[] assignments
) {
        public record Course(
                String id,
                String name,
                Unit[] units
        ) {
    }

        public record Unit(
                String id,
                String name
        ) {
    }

        public record Assignment(
                String id,
                String assignmentName,
                String courseName,
                String unitName,
                String assignmentType
        ) {
    }
}
