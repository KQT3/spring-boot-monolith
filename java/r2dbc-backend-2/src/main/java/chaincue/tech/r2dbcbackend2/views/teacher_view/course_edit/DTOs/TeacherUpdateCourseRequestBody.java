package chaincue.tech.r2dbcbackend2.views.teacher_view.course_edit.DTOs;


import chaincue.tech.r2dbcbackend2.masters.course_master.Course;

public record TeacherUpdateCourseRequestBody(
        String courseId,
        String newValue,
        Course.ChangeFieldName changeFieldName
) {
}
