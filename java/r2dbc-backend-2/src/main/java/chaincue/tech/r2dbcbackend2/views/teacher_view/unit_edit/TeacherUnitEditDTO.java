package chaincue.tech.r2dbcbackend2.views.teacher_view.unit_edit;

public record TeacherUnitEditDTO(
        String id,
        String name,
        String description,
        Assignment[] assignments,
        Course[] courses,
        Material[] materials,
        Material[] allMaterials,
        Tag[] tagsAttached,
        Tag[] allTags
) {
    public record Assignment(
            String id,
            String name
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

}
