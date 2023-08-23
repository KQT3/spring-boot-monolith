package chaincue.tech.r2dbcbackend2.views.teacher_portal.root;

import chaincue.tech.r2dbcbackend2.masters.teacher_master.Teacher;
import chaincue.tech.r2dbcbackend2.masters.teacher_master.TeacherService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

import static chaincue.tech.r2dbcbackend2.utilities.JWTDecoderUtil.getSubIdFromToken;


@RestController
@RequestMapping("teacher-root")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class TeacherRootViewController {
    private final TeacherService teacherService;

    @GetMapping("")
    public Mono<TeacherRootDTO> teacherRootView(@RequestHeader("Authorization") String token) {
        log.info(token);
        return teacherService.getOrCreateTeacher(getSubIdFromToken(token))
                .map(Param::new)
                .map(TeacherRootViewController::toDTO);
    }

    @PutMapping("mark-all-notify-as-read")
    public Mono<String> markAllNotifyAsRead(@RequestHeader("Authorization") String token) {
        return Mono.just("marked");
    }

    private static TeacherRootDTO toDTO(Param param) {
        return new TeacherRootDTO(
                param.getTeacher().getName(),
                param.getNotifications().stream().map(TeacherRootViewController::toDTO).toArray(TeacherRootDTO.Notification[]::new),
                param.getContacts().stream().map(TeacherRootViewController::toDTO).toArray(TeacherRootDTO.Contacts[]::new));
    }

    private static TeacherRootDTO.Notification toDTO(TeacherRootDTO.Notification notification) {
        return new TeacherRootDTO.Notification(
                notification.id(),
                notification.date(),
                notification.time(),
                notification.title(),
                notification.description(),
                notification.author(),
                notification.profileImg(),
                notification.read()
        );
    }

    private static TeacherRootDTO.Contacts toDTO(TeacherRootDTO.Contacts contact) {
        return new TeacherRootDTO.Contacts(
                contact.id(),
                contact.name(),
                contact.status());
    }

    @Data
    private static class Param {
        Teacher teacher;
        List<TeacherRootDTO.Notification> notifications = new ArrayList<>();
        List<TeacherRootDTO.Contacts> contacts= new ArrayList<>();

        public Param(Teacher teacher) {
            this.teacher = teacher;
            notifications.add(new TeacherRootDTO.Notification("1", "", "", "", "", "", "", false));
            contacts.add(new TeacherRootDTO.Contacts("1","", ""));
        }
    }
}
