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
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Function;

@Service
@AllArgsConstructor
public class CourseService implements CourseContract {
    private final CourseRepository courseRepository;
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;
    private final UnitRepository unitRepository;
    private final MaterialRepository materialRepository;
    private final TeacherCourseRelationRepository teacherCourseRelationRepository;
    private final StudentCourseRelationRepository studentCourseRelationRepository;
    private final UnitCourseRelationRepository unitCourseRelationRepository;
    private final MaterialRelationsRepository materialRelationsRepository;

    @Transactional
    @Override
    public Mono<Course> save(String name) {
        Course course = Course.create(Objects.equals(name, "") ? "default" : name);
        return courseRepository.save(course);
    }

    @Transactional
    @Override
    public Mono<Course> findCourseByIdWithRelations(String courseId) {
        return courseRepository.findById(courseId)
                .switchIfEmpty(Mono.error(new CourseNotFoundException(courseId)))
                .flatMap(course -> Mono.zip(
                                findTeacherCourseRelations(course),
                                findStudentCourseRelations(course),
                                findUnitCourseRelations(course),
                                findMaterialRelations(course)
                        )
                        .thenReturn(course));
    }

    @Override
    public Flux<Course> findAllWithRelations() {
        return courseRepository.findAll()
                .flatMap(course -> Mono.zip(
                                findTeacherCourseRelations(course),
                                findStudentCourseRelations(course),
                                findUnitCourseRelations(course),
                                findMaterialRelations(course)
                        )
                        .thenReturn(course));
    }

    @Override
    public Flux<Course> findAllWithRelations(Unit unit) {
        return unitCourseRelationRepository.findByUnitId(unit.getId())
                .flatMap(relation -> courseRepository.findById(relation.getCourseId()))
                .flatMap(course -> Mono.zip(
                                findTeacherCourseRelations(course),
                                findStudentCourseRelations(course),
                                findUnitCourseRelations(course),
                                findMaterialRelations(course)
                        )
                        .thenReturn(course));
    }


    private Mono<Course> findTeacherCourseRelations(Course course) {
        return teacherCourseRelationRepository.findByCourseId(course.getId())
                .flatMap(this::findTeacherCourseRelation)
                .collectList()
                .doOnNext(course::setTeachers)
                .thenReturn(course);
    }

    private Mono<Course> findStudentCourseRelations(Course course) {
        return studentCourseRelationRepository.findByCourseId(course.getId())
                .flatMap(this::findStudentCourseRelation)
                .collectList()
                .doOnNext(course::setStudents)
                .thenReturn(course);
    }

    private Mono<Course> findUnitCourseRelations(Course course) {
        return unitCourseRelationRepository.findByCourseId(course.getId())
                .flatMap(this::findUnitCourseRelation)
                .collectList()
                .doOnNext(course::setUnits)
                .thenReturn(course);
    }

    private Mono<Course> findMaterialRelations(Course course) {
        return materialRelationsRepository.findByCourseId(course.getId())
                .mapNotNull(MaterialRelation::getId)
                .collectList()
                .doOnNext(course::setMaterialRelations)
                .thenReturn(course);
    }

    private Mono<Teacher> findTeacherCourseRelation(TeacherCourseRelation teacherCourseRelation) {
        return teacherRepository.findById(teacherCourseRelation.getTeacherId());
    }

    private Mono<Student> findStudentCourseRelation(StudentCourseRelation studentCourseRelation) {
        return studentRepository.findById(studentCourseRelation.getStudentId());
    }

    private Mono<Unit> findUnitCourseRelation(UnitCourseRelation unitCourseRelation) {
        return unitRepository.findById(unitCourseRelation.getUnitId());
    }

    @Override
    public Mono<Course> findCourseById(String course) {
        return courseRepository.findById(course);
    }

    @Transactional
    @Override
    public Mono<MaterialRelation> addCourseToMaterial(String courseId, String materialId) {
        return materialRelationsRepository.existsByMaterialIdAndCourseId(materialId, courseId)
                .flatMap(doesRelationExist -> {
                    if (doesRelationExist) {
                        String errorMessage = String.format("Relation already exists for Material ID %s and Course ID %s", materialId, courseId);
                        return Mono.error(new RelationAlreadyExistsException(errorMessage));
                    }
                    return Mono.just(MaterialRelation.create(materialId, courseId, MaterialRelation.RelationType.COURSE));
                })
                .flatMap(materialRelationsRepository::save);
    }

    @Override
    public Mono<MaterialRelation> removeCourseFromMaterial(String courseId, String materialId) {
        return materialRelationsRepository.findByMaterialIdAndCourseId(materialId, courseId)
                .switchIfEmpty(Mono.error(new RelationNotFoundException(courseId + " | " + materialId)))
                .flatMap(relation -> materialRelationsRepository.deleteById(relation.getId())
                        .thenReturn(relation));
    }

    @Override
    @Transactional
    public Mono<String> deleteById(String courseId) {
        return courseRepository.existsById(courseId)
                .filter(Boolean::booleanValue)
                .switchIfEmpty(Mono.error(new CourseNotFoundException(courseId)))
                .then(Mono.defer(() -> Mono.when(
                        deleteStudentCourseRelations(courseId),
                        deleteUnitCourseRelations(courseId),
                        deleteTeacherCourseRelations(courseId),
                        deleteMaterialCourseRelations(courseId)
                )))
                .then(Mono.defer(() -> courseRepository.deleteById(courseId)))
                .thenReturn(courseId);
    }

    private Mono<Void> deleteStudentCourseRelations(String courseId) {
        return studentCourseRelationRepository.findByCourseId(courseId)
                .flatMap(relation -> studentCourseRelationRepository.deleteById(relation.getId()))
                .then();
    }

    private Mono<Void> deleteUnitCourseRelations(String courseId) {
        return unitCourseRelationRepository.findByCourseId(courseId)
                .flatMap(relation -> unitCourseRelationRepository.deleteById(relation.getId()))
                .then();
    }

    private Mono<Void> deleteTeacherCourseRelations(String courseId) {
        return teacherCourseRelationRepository.findByCourseId(courseId)
                .flatMap(relation -> teacherCourseRelationRepository.deleteById(relation.getId()))
                .then();
    }

    private Mono<Void> deleteMaterialCourseRelations(String courseId) {
        return materialRelationsRepository.findByCourseId(courseId)
                .flatMap(relation -> materialRelationsRepository.deleteById(relation.getId()))
                .then();
    }

    @Override
    @Transactional
    public Mono<Course> updateCourseField(String courseId, String newValue, Course.ChangeFieldName changeFieldName) {
        return courseRepository.findById(courseId)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Course with id " + courseId + " not found")))
                .flatMap(course -> {
                    switch (changeFieldName) {
                        case TITLE -> {
                            course.setName(newValue);
                            return courseRepository.updateCourseName(courseId, newValue)
                                    .thenReturn(course);
                        }
                        case DESCRIPTION -> {
                            course.setDescription(newValue);
                            return courseRepository.updateCourseDescription(courseId, newValue)
                                    .thenReturn(course);
                        }
                        case START_DATE -> {
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
                            LocalDate startDate = LocalDate.parse(newValue, formatter);
                            course.setStartDate(startDate);
                            return courseRepository.updateCourseStartDate(courseId, startDate)
                                    .thenReturn(course);
                        }
                        case END_DATE -> {
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
                            LocalDate endDate = LocalDate.parse(newValue, formatter);
                            course.setEndDate(endDate);
                            return courseRepository.updateCourseEndDate(courseId, endDate)
                                    .thenReturn(course);
                        }
                        case STATUS -> {
                            course.setStatus(Course.Status.valueOf(newValue));
                            return courseRepository.updateCourseStatus(courseId, Course.Status.valueOf(newValue))
                                    .thenReturn(course);
                        }
                        default -> {
                            return Mono.error(new IllegalArgumentException("Invalid ChangeFieldName: " + changeFieldName));
                        }
                    }
                });
    }

}
