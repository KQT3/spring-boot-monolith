package chaincue.tech.r2dbcbackend2.views.teacher_view.unit_edit.DTOs;


import chaincue.tech.r2dbcbackend2.masters.unit_master.Unit;

public record TeacherUpdateUnitFieldDTO(String unitId, String newValue, Unit.ChangeFieldName changeFieldName) {
}
