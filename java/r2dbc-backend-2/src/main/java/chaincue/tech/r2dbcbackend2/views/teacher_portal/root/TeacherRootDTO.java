package chaincue.tech.r2dbcbackend2.views.teacher_portal.root;

import java.util.Arrays;

public record TeacherRootDTO(
        String name,
        Notification[] notifications,
        Contacts[] contacts
) {
    @Override
    public String toString() {
        return "TeacherRootDTO{" +
                "name='" + name + '\'' +
                ", notifications=" + Arrays.toString(notifications) +
                ", contacts=" + Arrays.toString(contacts) +
                '}';
    }

    public record Notification(
            String id,
            String date,
            String time,
            String title,
            String description,
            String author,
            String profileImg,
            boolean read
    ) {
    }

    public record Contacts(
            String id,
            String name,
            String status
    ) {
    }

}

