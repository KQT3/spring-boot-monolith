package chaincue.tech.r2dbcbackend2.views.teacher_view.course_edit.DTOs;

public record TeacherUpdateUnitsRequestBody(
        String courseId,
        String unitId
) {
}
