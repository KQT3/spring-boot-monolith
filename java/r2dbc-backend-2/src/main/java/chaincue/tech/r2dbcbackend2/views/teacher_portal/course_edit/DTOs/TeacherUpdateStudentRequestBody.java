package chaincue.tech.r2dbcbackend2.views.teacher_portal.course_edit.DTOs;

public record TeacherUpdateStudentRequestBody(
        String courseId,
        String studentId
) {
}
