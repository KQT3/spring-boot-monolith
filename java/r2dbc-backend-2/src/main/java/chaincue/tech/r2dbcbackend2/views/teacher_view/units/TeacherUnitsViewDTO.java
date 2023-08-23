package chaincue.tech.r2dbcbackend2.views.teacher_view.units;

public record TeacherUnitsViewDTO(
        String id,
        String name,
        Unit[] units
) {

    public record Unit(
            String id,
            String name,
            Course[] courses,
            Tag[] tags
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

