package chaincue.tech.r2dbcbackend2.masters.teacher_master;

import chaincue.tech.r2dbcbackend2.exeptions.RelationAlreadyExistsException;
import chaincue.tech.r2dbcbackend2.masters.course_master.Course;
import chaincue.tech.r2dbcbackend2.masters.course_master.repositories.CourseRepository;
import chaincue.tech.r2dbcbackend2.masters.teacher_master.repositories.TeacherCourseRelationRepository;
import chaincue.tech.r2dbcbackend2.masters.teacher_master.repositories.TeacherRepository;
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
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TeacherServiceTest {
    @InjectMocks
    TeacherService teacherService;
    @Mock
    CourseRepository courseRepository;
    @Mock
    TeacherRepository teacherRepository;
    @Mock
    TeacherCourseRelationRepository teacherCourseRelationRepository;
    @Mock
    UserRepository userRepository;

    @Test
    void shouldSaveTeacherSuccessfully() {
        // Given
        String userName = "userName";
        String firstname = "firstname";
        String lastname = "lastname";
        var expectedTeacher = Teacher.create(userName);
        var expectedUser = User.createUser(userName, firstname, lastname);

        when(teacherRepository.save(any())).thenReturn(Mono.just(expectedTeacher));
        when(userRepository.save(any())).thenReturn(Mono.just(expectedUser));

        // When & Then
        StepVerifier.create(teacherService.save(userName, firstname, lastname))
                .assertNext(savedTeacher -> {
                    assertEquals(userName, savedTeacher.getName());
                    verify(teacherRepository, times(1)).save(any());
                    verify(userRepository, times(1)).save(any());
                })
                .verifyComplete();
    }

    @Test
    void getOrCreateTeacher_TeacherNotExistSuccess() {
        // Given
        String userId = "someUserId";
        String userName = "userName";
        String firstname = "firstname";
        String lastname = "lastname";
        var expectedTeacher = Teacher.create(userName);
        var expectedUser = User.createUser(userId, firstname, lastname);
        expectedUser.setTeacherId(expectedTeacher.getId());

        when(userRepository.findById(anyString())).thenReturn(Mono.empty());
        when(teacherRepository.findById(anyString())).thenReturn(Mono.empty());
        when(teacherRepository.save(any())).thenReturn(Mono.just(expectedTeacher));
        when(userRepository.save(any())).thenReturn(Mono.just(expectedUser));

        // When & Then
        StepVerifier.create(teacherService.getOrCreateTeacher(userId))
                .verifyComplete();
        verify(teacherRepository, times(1)).save(any());
        verify(userRepository, times(1)).save(any());
    }

    @Test
    void getOrCreateTeacher_TeacherExistSuccess() {
        // Given
        String userId = "someUserId";
        String userName = "userName";
        String firstname = "firstname";
        String lastname = "lastname";
        var expectedTeacher = Teacher.create(userName);
        var expectedUser = User.createUser(userId, firstname, lastname);
        expectedUser.setTeacherId(expectedTeacher.getId());

        when(userRepository.findById(userId)).thenReturn(Mono.just(expectedUser));
        when(teacherRepository.findById(anyString())).thenReturn(Mono.just(expectedTeacher));

        // When & Then
        StepVerifier.create(teacherService.getOrCreateTeacher(userId))
                .expectNextMatches(teacher -> true)
                .verifyComplete();
        verify(teacherRepository, times(0)).save(any());
        verify(userRepository, times(0)).save(any());
    }

    @Test
    public void getTeacherById() {
        // Given
        String teacherId = "12345";
        Teacher mockTeacher = Teacher.create("John Doe");

        when(teacherRepository.findById(teacherId)).thenReturn(Mono.just(mockTeacher));

        // When & Then
        StepVerifier.create(teacherService.getTeacherById(teacherId))
                .assertNext(teacher -> {
                    assertEquals(mockTeacher.getName(), teacher.getName());
                })
                .verifyComplete();
        verify(teacherRepository).findById(teacherId);
    }


    @Test
    void getTeacherByIdWithCoursesSuccess() {
        // Given
        Teacher teacher = Teacher.create("");
        Course course1 = Course.create("course1");
        Course course2 = Course.create("course2");

        when(teacherRepository.findById(anyString())).thenReturn(Mono.just(teacher));
        when(teacherCourseRelationRepository.findByTeacherId(anyString())).thenReturn(Flux.fromIterable(Arrays.asList(
                TeacherCourseRelation.create(teacher.getId(), course1.getId()),
                TeacherCourseRelation.create(teacher.getId(), course2.getId())
        )));
        when(courseRepository.findAllById(anyList())).thenReturn(Flux.fromIterable(List.of(course2, course1)));

        // When & Then
        StepVerifier.create(teacherService.findTeacherByIdWithCourses(teacher.getId()))
                .assertNext(resultingTeacher -> {
                    assertEquals(teacher, resultingTeacher);
                    assertNotNull(resultingTeacher.getCourses());
                    assertEquals(2, resultingTeacher.getCourses().size());
                    assertTrue(resultingTeacher.getCourses().contains(course1));
                    assertTrue(resultingTeacher.getCourses().contains(course2));
                })
                .verifyComplete();
    }

    @Test
    void addTeacherToCourse_RelationExists() {
        // Given
        String teacherId = "123";
        String courseId = "456";

        when(teacherCourseRelationRepository.existsByTeacherIdAndCourseId(teacherId, courseId)).thenReturn(Mono.just(true));

        // When & Then
        StepVerifier.create(teacherService.addTeacherToCourse(teacherId, courseId))
                .expectErrorMatches(throwable -> throwable instanceof RelationAlreadyExistsException &&
                        throwable.getMessage().contains("Relation already exists"))
                .verify();
    }

    @Test
    void addTeacherToCourse_RelationDoesNotExist() {
        // Given
        String teacherId = "123";
        String courseId = "456";
        TeacherCourseRelation expectedRelation = TeacherCourseRelation.create(teacherId, courseId);

        when(teacherCourseRelationRepository.existsByTeacherIdAndCourseId(anyString(), anyString())).thenReturn(Mono.just(false));
        when(teacherCourseRelationRepository.save(any())).thenReturn(Mono.just(expectedRelation));

        // When & Then
        StepVerifier.create(teacherService.addTeacherToCourse(teacherId, courseId))
                .assertNext(relationResult -> {
                    assertEquals(expectedRelation, relationResult);
                    verify(teacherCourseRelationRepository, times(1)).save(any());
                })
                .verifyComplete();
    }

}
