package chaincue.tech.r2dbcbackend2.views.teacher_view.courses;

public record TeacherCoursesViewDTO(
        String teacherId,
        String name,
        Course[] myCourses,
        Course[] allCourses
) {
        public record Course(
                String id,
                String name,
                String startDate,
                String endDate,
                String status,
                Unit[] units
        ) {
    }

        public record Unit(
                String id,
                String name
        ) {
    }

}

