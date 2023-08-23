package chaincue.tech.r2dbcbackend2.views.teacher_view.course_edit.DTOs;

public record TeacherUpdateMaterialRequestBody(
        String courseId,
        String materialId
) {
}
