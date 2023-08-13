package chaincue.tech.jpabackend.masters.teacher_master

import org.springframework.data.jpa.repository.JpaRepository

interface TeacherRepository : JpaRepository<Teacher, String> {
}
