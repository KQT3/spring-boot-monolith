package chaincue.tech.r2dbcbackend2.masters.course_master;

import chaincue.tech.r2dbcbackend2.exeptions.CourseNotFoundException;
import chaincue.tech.r2dbcbackend2.exeptions.RelationAlreadyExistsException;
import chaincue.tech.r2dbcbackend2.exeptions.RelationNotFoundException;
import chaincue.tech.r2dbcbackend2.masters.course_master.repositories.CourseRepository;
import chaincue.tech.r2dbcbackend2.masters.material_master.MaterialRelation;
import chaincue.tech.r2dbcbackend2.masters.material_master.repositories.MaterialRelationsRepository;
import chaincue.tech.r2dbcbackend2.masters.material_master.repositories.MaterialRepository;
import chaincue.tech.r2dbcbackend2.masters.student_master.Student;
import chaincue.tech.r2dbcbackend2.masters.student_master.StudentCourseRelation;
import chaincue.tech.r2dbcbackend2.masters.student_master.repositories.StudentCourseRelationRepository;
import chaincue.tech.r2dbcbackend2.masters.student_master.repositories.StudentRepository;
import chaincue.tech.r2dbcbackend2.masters.teacher_master.Teacher;
import chaincue.tech.r2dbcbackend2.masters.teacher_master.TeacherCourseRelation;
import chaincue.tech.r2dbcbackend2.masters.teacher_master.repositories.TeacherCourseRelationRepository;
import chaincue.tech.r2dbcbackend2.masters.teacher_master.repositories.TeacherRepository;
import chaincue.tech.r2dbcbackend2.masters.unit_master.Unit;
import chaincue.tech.r2dbcbackend2.masters.unit_master.UnitCourseRelation;
import chaincue.tech.r2dbcbackend2.masters.unit_master.repositories.UnitCourseRelationRepository;
import chaincue.tech.r2dbcbackend2.masters.unit_master.repositories.UnitRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CourseServiceTest {
    @InjectMocks
    CourseService courseService;
    @Mock
    CourseRepository courseRepository;
    @Mock
    TeacherRepository teacherRepository;
    @Mock
    StudentRepository studentRepository;
    @Mock
    UnitRepository unitRepository;
    @Mock
    MaterialRepository materialRepository;
    @Mock
    TeacherCourseRelationRepository teacherCourseRelationRepository;
    @Mock
    StudentCourseRelationRepository studentCourseRelationRepository;
    @Mock
    UnitCourseRelationRepository unitCourseRelationRepository;
    @Mock
    MaterialRelationsRepository materialRelationsRepository;

    @Test
    void save_WithName_ShouldSaveCourse() {
        // Given
        String courseName = "Test Course";
        Course expectedCourse = Course.create(courseName);

        when(courseRepository.save(any(Course.class))).thenReturn(Mono.just(expectedCourse));

        // When & Then
        StepVerifier.create(courseService.save(courseName))
                .assertNext(course -> {
                    assertEquals(courseName, course.getName());
                    verify(courseRepository).save(any(Course.class));
                })
                .verifyComplete();
    }

    @Test
    public void FindAllWithRelationsSuccess() {
        // Given
        Course course = Course.create("");

        when(courseRepository.findAll()).thenReturn(Flux.just(course));
        when(teacherCourseRelationRepository.findByCourseId(anyString())).thenReturn(Flux.empty());
        when(studentCourseRelationRepository.findByCourseId(anyString())).thenReturn(Flux.empty());
        when(unitCourseRelationRepository.findByCourseId(anyString())).thenReturn(Flux.empty());
        when(materialRelationsRepository.findByCourseId(anyString())).thenReturn(Flux.empty());

        // When & Then
        StepVerifier.create(courseService.findAllWithRelations())
                .assertNext(courseResult -> assertEquals(courseResult, course))
                .verifyComplete();
    }

    @Test
    public void FindAllWithRelationsForUnitSuccess() {
        // Given
        var unit = Unit.create("");
        Course course = Course.create("");
        var unitCourseRelation = UnitCourseRelation.create(unit.getId(), course.getId());

        when(unitCourseRelationRepository.findByUnitId(anyString())).thenReturn(Flux.just(unitCourseRelation));
        when(courseRepository.findById(anyString())).thenReturn(Mono.just(course));
        when(teacherCourseRelationRepository.findByCourseId(anyString())).thenReturn(Flux.empty());
        when(studentCourseRelationRepository.findByCourseId(anyString())).thenReturn(Flux.empty());
        when(unitCourseRelationRepository.findByCourseId(anyString())).thenReturn(Flux.empty());
        when(materialRelationsRepository.findByCourseId(anyString())).thenReturn(Flux.empty());

        // When & Then
        StepVerifier.create(courseService.findAllWithRelations(unit))
                .assertNext(courseResult -> assertEquals(courseResult, course))
                .verifyComplete();
    }

    @Test
    void addCourseToMaterial_NoExistingRelation_ShouldSaveAndReturnRelation() {
        // Given
        String materialId = "material1";
        String courseId = "course1";
        var expectedRelation = MaterialRelation.create(materialId, courseId, MaterialRelation.RelationType.COURSE);

        when(materialRelationsRepository.existsByMaterialIdAndCourseId(materialId, courseId)).thenReturn(Mono.just(false));
        when(materialRelationsRepository.save(any(MaterialRelation.class))).thenReturn(Mono.just(expectedRelation));

        // When & Then
        StepVerifier.create(courseService.addCourseToMaterial(courseId, materialId))
                .assertNext(relation -> {
                    assertEquals(materialId, relation.getMaterialId());
                    assertEquals(courseId, relation.getCourseId());
                    assertEquals(MaterialRelation.RelationType.COURSE, relation.getRelationType());
                    verify(materialRelationsRepository).save(any());
                })
                .verifyComplete();
    }

    @Test
    void addCourseToMaterial_ExistingRelation_ShouldError() {
        // Given
        String materialId = "material1";
        String courseId = "course1";

        when(materialRelationsRepository.existsByMaterialIdAndCourseId(materialId, courseId)).thenReturn(Mono.just(true));

        // When & Then
        StepVerifier.create(courseService.addCourseToMaterial(courseId, materialId))
                .verifyError(RelationAlreadyExistsException.class);
    }

    @Test
    void findCourseById_CourseExists_ShouldReturnCourse() {
        // Given
        Course course = Course.create("");
        when(courseRepository.findById(anyString())).thenReturn(Mono.just(course));

        // When & Then
        StepVerifier.create(courseService.findCourseById(course.getId()))
                .assertNext(courseResult -> assertEquals(course.getId(), courseResult.getId()))
                .verifyComplete();
    }

    @Test
    void findCourseByIdWithRelations_CourseExists_ShouldReturnCourse() {
        // Given
        var course = Course.create("");
        var teacherCourseRelation = TeacherCourseRelation.create("", course.getId());
        var teacher = Teacher.create("");
        var student = Student.create("");
        var unit = Unit.create("");
        var studentCourseRelation = StudentCourseRelation.create(student.getId(), course.getId());
        var unitCourseRelation = UnitCourseRelation.create(unit.getId(), course.getId());
        var materialRelation = MaterialRelation.create("", "", MaterialRelation.RelationType.COURSE);

        when(courseRepository.findById(anyString())).thenReturn(Mono.just(course));
        when(teacherCourseRelationRepository.findByCourseId(anyString())).thenReturn(Flux.just(teacherCourseRelation));
        when(teacherRepository.findById(anyString())).thenReturn(Mono.just(teacher));
        when(studentCourseRelationRepository.findByCourseId(anyString())).thenReturn(Flux.just(studentCourseRelation));
        when(studentRepository.findById(anyString())).thenReturn(Mono.just(student));
        when(unitCourseRelationRepository.findByCourseId(anyString())).thenReturn(Flux.just(unitCourseRelation));
        when(unitRepository.findById(anyString())).thenReturn(Mono.just(unit));
        when(materialRelationsRepository.findByCourseId(anyString())).thenReturn(Flux.just(materialRelation));

        // When & Then
        StepVerifier.create(courseService.findCourseByIdWithRelations(course.getId()))
                .assertNext(courseResult -> assertEquals(course.getId(), courseResult.getId()))
                .verifyComplete();
    }

    @Test
    void findCourseByIdWithRelations_CourseDoesNotExist_ShouldThrowCourseNotFoundException() {
        // Given
        String courseId = "course1";
        when(courseRepository.findById(courseId)).thenReturn(Mono.empty());

        // When & Then
        StepVerifier.create(courseService.findCourseByIdWithRelations(courseId))
                .expectError(CourseNotFoundException.class)
                .verify();
    }

    @Test
    void deleteById_CourseExists_ShouldReturnCourseId() {
        // Given
        String courseId = "course1";

        when(courseRepository.existsById(courseId)).thenReturn(Mono.just(true));
        when(studentCourseRelationRepository.findByCourseId(anyString())).thenReturn(Flux.empty());
        when(unitCourseRelationRepository.findByCourseId(anyString())).thenReturn(Flux.empty());
        when(teacherCourseRelationRepository.findByCourseId(anyString())).thenReturn(Flux.empty());
        when(materialRelationsRepository.findByCourseId(anyString())).thenReturn(Flux.empty());
        when(courseRepository.deleteById(courseId)).thenReturn(Mono.empty());

        // When & Then
        StepVerifier.create(courseService.deleteById(courseId))
                .assertNext(returnedCourseId -> {
                    assertEquals(courseId, returnedCourseId);
                    verify(courseRepository).deleteById(anyString());
                })
                .verifyComplete();
    }

    @Test
    void deleteById_CourseDoesNotExist_ShouldThrowCourseNotFoundException() {
        // Given
        Course course = Course.create("");
        when(courseRepository.existsById(anyString())).thenReturn(Mono.just(false));

        // When & Then
        StepVerifier.create(courseService.deleteById(course.getId()))
                .expectError(CourseNotFoundException.class)
                .verify();
    }

    @Test
    public void removeCourseFromMaterial_RelationDoesNotExist_ShouldError() {
        // Given
        String courseId = "testCourseId";
        String materialId = "testMaterialId";

        when(materialRelationsRepository.findByMaterialIdAndCourseId(materialId, courseId)).thenReturn(Mono.empty());

        // When & Then
        StepVerifier.create(courseService.removeCourseFromMaterial(courseId, materialId))
                .expectError(RelationNotFoundException.class)
                .verify();
        verify(materialRelationsRepository, never()).deleteById(anyString());
    }

    @Test
    public void updateCourseField_Title_ShouldUpdateTitle() {
        // Given
        String courseId = "testCourseId";
        Course mockCourse = Course.create("course");
        String expectedNewValue = "New Title";

        when(courseRepository.findById(courseId)).thenReturn(Mono.just(mockCourse));
        when(courseRepository.updateCourseName(courseId, expectedNewValue)).thenReturn(Mono.empty());
        // When & Then
        StepVerifier.create(courseService.updateCourseField(courseId, expectedNewValue, Course.ChangeFieldName.TITLE))
                .assertNext(course -> assertEquals(expectedNewValue, course.getName()))
                .verifyComplete();
    }

    @Test
    public void updateCourseField_Description_ShouldUpdateDescription() {
        // Given
        String courseId = "testCourseId";
        Course mockCourse = Course.create("course");
        String expectedNewValue = "New Description";

        when(courseRepository.findById(courseId)).thenReturn(Mono.just(mockCourse));
        when(courseRepository.updateCourseDescription(courseId, expectedNewValue)).thenReturn(Mono.empty());

        // When & Then
        StepVerifier.create(courseService.updateCourseField(courseId, expectedNewValue, Course.ChangeFieldName.DESCRIPTION))
                .assertNext(course -> assertEquals(expectedNewValue, course.getDescription()))
                .verifyComplete();
    }

    @Test
    public void updateCourseField_StartDate_ShouldUpdateStartDate() {
        // Given
        String courseId = "testCourseId";
        Course mockCourse = Course.create("course");
        String newValue = "01-01-2023";
        LocalDate expectedStartDate = LocalDate.parse(newValue, DateTimeFormatter.ofPattern("MM-dd-yyyy"));

        when(courseRepository.findById(courseId)).thenReturn(Mono.just(mockCourse));
        when(courseRepository.updateCourseStartDate(courseId, expectedStartDate)).thenReturn(Mono.empty());

        // When & Then
        StepVerifier.create(courseService.updateCourseField(courseId, newValue, Course.ChangeFieldName.START_DATE))
                .assertNext(course -> assertEquals(expectedStartDate, course.getStartDate()))
                .verifyComplete();
    }

    @Test
    public void updateCourseField_EndDate_ShouldUpdateEndDate() {
        // Given
        String courseId = "testCourseId";
        Course mockCourse = Course.create("course");
        String newValue = "01-01-2024";
        LocalDate expectedEndDate = LocalDate.parse(newValue, DateTimeFormatter.ofPattern("MM-dd-yyyy"));

        when(courseRepository.findById(courseId)).thenReturn(Mono.just(mockCourse));
        when(courseRepository.updateCourseEndDate(courseId, expectedEndDate)).thenReturn(Mono.empty());

        // When & Then
        StepVerifier.create(courseService.updateCourseField(courseId, newValue, Course.ChangeFieldName.END_DATE))
                .assertNext(course -> assertEquals(expectedEndDate, course.getEndDate()))
                .verifyComplete();
    }

    @Test
    public void updateCourseField_Status_ShouldUpdateStatus() {
        // Given
        String courseId = "testCourseId";
        Course mockCourse = Course.create("course");
        String newValue = "ACTIVE";
        Course.Status expectedStatus = Course.Status.valueOf(newValue);

        when(courseRepository.findById(courseId)).thenReturn(Mono.just(mockCourse));
        when(courseRepository.updateCourseStatus(courseId, expectedStatus)).thenReturn(Mono.empty());

        StepVerifier.create(courseService.updateCourseField(courseId, newValue, Course.ChangeFieldName.STATUS))
                .assertNext(course -> assertEquals(expectedStatus, course.getStatus()))
                .verifyComplete();
    }

    @Test
    public void removeCourseFromMaterial_RelationExists_ShouldRemoveAndReturn() {
        // Given
        String courseId = "testCourseId";
        String materialId = "testMaterialId";
        MaterialRelation mockRelation = MaterialRelation.create("", "", MaterialRelation.RelationType.COURSE);

        when(materialRelationsRepository.findByMaterialIdAndCourseId(materialId, courseId)).thenReturn(Mono.just(mockRelation));
        when(materialRelationsRepository.deleteById(mockRelation.getId())).thenReturn(Mono.empty());

        //When & Then
        StepVerifier.create(courseService.removeCourseFromMaterial(courseId, materialId))
                .assertNext(relation -> {
                    assertEquals(mockRelation, relation);
                    verify(materialRelationsRepository).deleteById(mockRelation.getId());
                })
                .verifyComplete();
    }

}
