package chaincue.tech.r2dbcbackend2.views.teacher_portal.unit_edit.DTOs;

public record TeacherCreateAssignmentReqBody(String name,
                                             String courseId,
                                             String unitId,
                                             String assignmentType
) {
}
