package chaincue.tech.r2dbcbackend2.views.teacher_portal.dashboard;

import java.time.LocalDate;

public record TeacherDashboardViewDTO(
        String name,
        Student[] students,
        Teacher[] teachers,
        Course[] courses
) {

    public record Student(
            String id,
            String name
    ) {
    }

    public record Teacher(
            String id,
            String name
    ) {
    }

    public record Course(
            String id,
            String name,
            String description,
            int events,
            LocalDate startDate,
            LocalDate endDate,
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

