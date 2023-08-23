package chaincue.tech.r2dbcbackend2.masters.student_master;

import chaincue.tech.r2dbcbackend2.exeptions.RelationAlreadyExistsException;
import chaincue.tech.r2dbcbackend2.exeptions.RelationNotFoundException;
import chaincue.tech.r2dbcbackend2.exeptions.StudentNotFoundException;
import chaincue.tech.r2dbcbackend2.masters.course_master.Course;
import chaincue.tech.r2dbcbackend2.masters.course_master.repositories.CourseRepository;
import chaincue.tech.r2dbcbackend2.masters.student_master.repositories.StudentCourseRelationRepository;
import chaincue.tech.r2dbcbackend2.masters.student_master.repositories.StudentRepository;
import chaincue.tech.r2dbcbackend2.masters.user_master.User;
import chaincue.tech.r2dbcbackend2.masters.user_master.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {
    @InjectMocks
    StudentService studentService;
    @Mock
    StudentRepository studentRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    StudentCourseRelationRepository studentCourseRelationRepository;
    @Mock
    CourseRepository courseRepository;

    @Test
    void save_ShouldSaveStudentAndUserAndReturnStudent() {
        // Given
        String userName = "testUserName";
        String firstName = "John";
        String lastName = "Doe";

        var mockUser = User.createUser(userName, firstName, lastName);
        Student mockStudent = Student.create(userName);
        mockStudent.setUserId(mockUser.getId());
        mockUser.setStudentId(mockStudent.getId());

        when(studentRepository.save(any(Student.class))).thenReturn(Mono.just(mockStudent));
        when(userRepository.save(any(User.class))).thenReturn(Mono.just(mockUser));

        // When & Then
        StepVerifier.create(studentService.save(userName, firstName, lastName))
                .assertNext(student -> {
                    assertEquals(mockStudent.getName(), student.getName());
                    verify(studentRepository).save(any());
                    verify(userRepository).save(any());
                })
                .verifyComplete();
    }

    @Test
    void addStudentToCourse_ShouldSaveRelation_WhenRelationDoesNotExist() {
        // Given
        String studentId = "student123";
        String courseId = "course123";

        when(studentCourseRelationRepository.existsByStudentIdAndCourseId(studentId, courseId)).thenReturn(Mono.just(false));
        when(studentCourseRelationRepository.save(any(StudentCourseRelation.class))).thenReturn(Mono.just(StudentCourseRelation.create(studentId, courseId)));

        // When & Then
        StepVerifier.create(studentService.addStudentToCourse(studentId, courseId))
                .assertNext(relation -> {
                    assertEquals(studentId, relation.getStudentId());
                    assertEquals(courseId, relation.getCourseId());
                    verify(studentCourseRelationRepository).save(any());
                })
                .verifyComplete();
    }

    @Test
    void addStudentToCourse_ShouldThrowException_WhenRelationExists() {
        // Given
        String studentId = "student123";
        String courseId = "course123";

        when(studentCourseRelationRepository.existsByStudentIdAndCourseId(studentId, courseId)).thenReturn(Mono.just(true));

        // When & Then
        StepVerifier.create(studentService.addStudentToCourse(studentId, courseId))
                .expectErrorMatches(throwable -> throwable instanceof RelationAlreadyExistsException &&
                        throwable.getMessage().contains("Relation already exists for Student ID"))
                .verify();
    }

    @Test
    void removeStudentFromCourse_ShouldDeleteAndReturnRelation_WhenRelationExists() {
        // Given
        String studentId = "student123";
        String courseId = "course123";
        StudentCourseRelation mockRelation = StudentCourseRelation.create(studentId, courseId);

        when(studentCourseRelationRepository.findByStudentIdAndCourseId(studentId, courseId)).thenReturn(Mono.just(mockRelation));
        when(studentCourseRelationRepository.deleteById(mockRelation.getId())).thenReturn(Mono.empty());

        // When & Then
        StepVerifier.create(studentService.removeStudentFromCourse(studentId, courseId))
                .assertNext(relation -> {
                    assertEquals(studentId, relation.getStudentId());
                    assertEquals(courseId, relation.getCourseId());
                    verify(studentCourseRelationRepository).deleteById(anyString());
                })
                .verifyComplete();
    }

    @Test
    void removeStudentFromCourse_ShouldThrowException_WhenRelationDoesNotExist() {
        // Given
        String studentId = "student123";
        String courseId = "course123";

        when(studentCourseRelationRepository.findByStudentIdAndCourseId(studentId, courseId)).thenReturn(Mono.empty());

        // When & Then
        StepVerifier.create(studentService.removeStudentFromCourse(studentId, courseId))
                .expectErrorMatches(throwable -> throwable instanceof RelationNotFoundException &&
                        throwable.getMessage().contains(studentId + " | " + courseId))
                .verify();
    }

    @Test
    void findStudentByIdWithCourses_ShouldReturnStudentWithCourses_WhenStudentExists() {
        // Given
        Student student = Student.create("");
        var course1 = Course.create("Course1");
        var course2 = Course.create("Course2");
        StudentCourseRelation studentCourseRelation1 = StudentCourseRelation.create(student.getId(), course1.getId());
        StudentCourseRelation studentCourseRelation2 = StudentCourseRelation.create(student.getId(), course2.getId());

        when(studentRepository.findById(anyString())).thenReturn(Mono.just(student));
        when(studentCourseRelationRepository.findByStudentId(anyString())).thenReturn(Flux.just(studentCourseRelation1, studentCourseRelation2));
        when(courseRepository.findAllById(anyList())).thenReturn(Flux.just(course1, course2));

        // When & Then
        StepVerifier.create(studentService.findStudentByIdWithCourses(student.getId()))
                .assertNext(studentResult -> {
                    assertEquals(studentResult, student);
                    assertTrue(studentResult.getCourses().containsAll(List.of(course2, course1)));
                })
                .verifyComplete();
    }

    @Test
    void findStudentByIdWithCourses_ShouldThrowException_WhenStudentDoesNotExist() {
        // Given
        String studentId = "student123";
        when(studentRepository.findById(studentId)).thenReturn(Mono.empty());

        // When & Then
        StepVerifier.create(studentService.findStudentByIdWithCourses(studentId))
                .expectErrorMatches(throwable -> throwable instanceof StudentNotFoundException &&
                        throwable.getMessage().contains(studentId))
                .verify();
    }

    @Test
    void findAllWithRelations_ShouldReturnAllStudentsWithCourses() {
        // Given
        Student student1 = Student.create("Student1");
        Student student2 = Student.create("Student2");
        Course course1 = Course.create("Course1");
        Course course2 = Course.create("Course2");
        StudentCourseRelation studentCourseRelation1 = StudentCourseRelation.create(student1.getId(), course1.getId());
        StudentCourseRelation studentCourseRelation2 = StudentCourseRelation.create(student2.getId(), course2.getId());

        when(studentRepository.findAll()).thenReturn(Flux.just(student1, student2));
        when(studentCourseRelationRepository.findByStudentId(anyString())).thenReturn(Flux.just(studentCourseRelation1, studentCourseRelation2));
        when(courseRepository.findAllById(anyList())).thenReturn(Flux.just(course1, course2));

        // When & Then
        StepVerifier.create(studentService.findAllWithRelations())
                .assertNext(student -> {
                    assertEquals(student1, student);
                    assertTrue(student.getCourses().contains(course2));
                })
                .assertNext(student -> {
                    assertEquals(student2, student);
                    assertTrue(student.getCourses().contains(course1));
                })
                .verifyComplete();
    }

    @Test
    void findByNameContainingIgnoreCase() {
        // Given
        var student1 = Student.create("student1");
        var student2 = Student.create("student2");
        String[] searchValues = {"student1", "student2"};

        when(studentRepository.findAll()).thenReturn(Flux.just(student1, student2));
        when(studentCourseRelationRepository.findByStudentId((anyString()))).thenReturn(Flux.empty());
        when(courseRepository.findAllById(anyList())).thenReturn(Flux.empty());

        // When & Then
        StepVerifier.create(studentService.findByNameContainingIgnoreCase(searchValues))
                .expectNext(student1)
                .expectNext(student2)
                .verifyComplete();
    }
}

