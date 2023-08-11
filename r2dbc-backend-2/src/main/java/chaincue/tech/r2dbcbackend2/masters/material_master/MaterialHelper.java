package chaincue.tech.r2dbcbackend2.masters.material_master;

import chaincue.tech.r2dbcbackend2.exeptions.MaterialNotFoundException;
import chaincue.tech.r2dbcbackend2.masters.course_master.CourseRepository;
import chaincue.tech.r2dbcbackend2.masters.student_master.StudentRepository;
import chaincue.tech.r2dbcbackend2.masters.teacher_master.TeacherRepository;
import chaincue.tech.r2dbcbackend2.masters.unit_master.UnitRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class MaterialHelper {
    private final MaterialRepository materialRepository;
    private final CourseRepository courseRepository;
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;
    private final UnitRepository unitRepository;
    private final MaterialRelationsRepository materialRelationsRepository;

    @Transactional
    public Mono<Material> save(String name, Material.MaterialType materialType, String uri) {
        var material = Material.create(name, materialType, uri);
        return materialRepository.save(material);
    }

    @Transactional
    public Mono<Material> findMaterialById(String materialId) {
        return materialRepository.findById(materialId);
    }

    @Transactional
    public Mono<Material> findMaterialByIdWithRelations(String materialId) {
        return materialRepository.findById(materialId)
                .switchIfEmpty(Mono.error(new MaterialNotFoundException(materialId)))
                .flatMap(this::findMaterialRelations);
    }

    private Mono<Material> findMaterialRelations(Material material) {
        return materialRelationsRepository.findByMaterialId(material.getId())
                .mapNotNull(MaterialRelation::getId)
                .collectList()
                .doOnNext(material::setMaterialRelations)
                .thenReturn(material);
    }
}
