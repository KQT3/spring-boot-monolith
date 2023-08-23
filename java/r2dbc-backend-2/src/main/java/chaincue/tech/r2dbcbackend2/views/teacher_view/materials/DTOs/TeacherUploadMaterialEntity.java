package chaincue.tech.r2dbcbackend2.views.teacher_view.materials.DTOs;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TeacherUploadMaterialEntity {
    String id;
    String name;
    TeacherMaterialTypes teacherMaterialTypes;
    String description;
}
