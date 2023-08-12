package chaincue.tech.r2dbcbackend2.masters.course_master;

import chaincue.tech.r2dbcbackend2.exeptions.CourseNotFoundException;
import chaincue.tech.r2dbcbackend2.exeptions.MaterialNotFoundException;
import chaincue.tech.r2dbcbackend2.masters.material_master.MaterialRelation;
import chaincue.tech.r2dbcbackend2.masters.material_master.MaterialRelationsRepository;
import chaincue.tech.r2dbcbackend2.masters.material_master.MaterialRepository;
import chaincue.tech.r2dbcbackend2.masters.student_master.Student;
import chaincue.tech.r2dbcbackend2.masters.student_master.StudentCourseRelation;
import chaincue.tech.r2dbcbackend2.masters.student_master.StudentCourseRelationRepository;
import chaincue.tech.r2dbcbackend2.masters.student_master.StudentRepository;
import chaincue.tech.r2dbcbackend2.masters.teacher_master.Teacher;
import chaincue.tech.r2dbcbackend2.masters.teacher_master.TeacherCourseRelation;
import chaincue.tech.r2dbcbackend2.masters.teacher_master.TeacherCourseRelationRepository;
import chaincue.tech.r2dbcbackend2.masters.teacher_master.TeacherRepository;
import chaincue.tech.r2dbcbackend2.masters.unit_master.Unit;
import chaincue.tech.r2dbcbackend2.masters.unit_master.UnitCourseRelation;
import chaincue.tech.r2dbcbackend2.masters.unit_master.UnitCourseRelationRepository;
import chaincue.tech.r2dbcbackend2.masters.unit_master.UnitRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class CourseHelper {
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
    public Mono<Course> save(String name) {
        Course course = Course.create(name);
        return courseRepository.save(course);
    }

    @Transactional
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

    @Transactional
    public Mono<Course> findCourseById(String course) {
        return courseRepository.findById(course);
    }

    @Transactional
    public Mono<MaterialRelation> addCourseToMaterial(String courseId, String materialId) {
        var materialRelation = MaterialRelation.create(materialId, courseId, MaterialRelation.RelationType.COURSE);
        return Mono.zip(courseRepository.existsById(courseId), materialRepository.existsById(materialId),
                        (courseExists, materialExists) -> {
                            if (!courseExists) throw new CourseNotFoundException(courseId);
                            if (!materialExists) throw new MaterialNotFoundException(materialId);
                            return materialRelation;
                        })
                .flatMap(materialRelationsRepository::save);
    }
}
