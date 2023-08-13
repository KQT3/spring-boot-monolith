package chaincue.tech.jpabackend.masters.student_master

import chaincue.tech.jpabackend.masters.teacher_master.Course
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CourseRepository : JpaRepository<Course, String>
