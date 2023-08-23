package chaincue.tech.r2dbcbackend2.views.teacher_portal.course_edit;

public record TeacherCourseEditDTO(
        String id,
        String courseName,
        String courseDescription,
        String startDate,
        String endDate,
        String status,
        Unit[] units,
        Unit[] allUnits,
        Material[] materials,
        Material[] allMaterials,
        Student[] allStudents,
        Student[] studentsAdded
) {
        public record Unit(
                String id,
                String name,
                boolean attachedToCourse
        ) {
    }

        public record Material(
                String id,
                String name,
                String uri,
                boolean attachedToCourse,
                Course[] materialCourseRelations,
                Tag[] tagsAttached,
                String type
        ) {
    }

        public record Course(
                String id,
                String name
        ) {
    }

        public record Tag(
                String id,
                String name
        ) {
    }

        public record Student(
                String id,
                String name,
                boolean attachedToCourse,
                Course[] studentCourse
        ) {
    }
}
