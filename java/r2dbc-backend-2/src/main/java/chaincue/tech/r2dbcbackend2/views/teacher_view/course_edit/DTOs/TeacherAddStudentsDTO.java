package chaincue.tech.r2dbcbackend2.views.teacher_view.course_edit.DTOs;

public record TeacherAddStudentsDTO(
        String courseId,
        Student[] students
) {
    public record Student(
            String id,
            String name
    ) {
    }
}
