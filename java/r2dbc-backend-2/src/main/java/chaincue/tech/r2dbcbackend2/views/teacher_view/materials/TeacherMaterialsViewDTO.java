package chaincue.tech.r2dbcbackend2.views.teacher_view.materials;

public record TeacherMaterialsViewDTO(
        String id,
        String name,
        Material[] allMaterials,
        Course[] allCourses,
        Unit[] allUnits,
        Assignment[] allAssignments,
        Tag[] allTags
) {
        public record Material(
                String id,
                String name,
                String materialType,
                String description,
                String uri,
                Course[] materialCourseRelations,
                Unit[] materialUnitRelations,
                Assignment[] materialAssignmentRelations,
                Tag[] tags
        ) {
    }

        public record Course(
                String id,
                String name
        ) {
    }

        public record Unit(
                String id,
                String name
        ) {
    }

        public record Assignment(
                String id,
                String name
        ) {
    }

        public record Tag(
                String id,
                String name
        ) {
    }

}
