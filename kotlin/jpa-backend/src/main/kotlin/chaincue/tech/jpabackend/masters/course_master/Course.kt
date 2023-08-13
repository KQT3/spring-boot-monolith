package chaincue.tech.jpabackend.masters.teacher_master

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.ManyToMany
import lombok.ToString
import java.util.*

@ToString(exclude = ["teacherCourseRelations", "studentCourseRelations"])
@Entity(name = "courses")
data class Course(
        @Id
        val id: String,
        val name: String,
        @ManyToMany(mappedBy = "teacherCourseRelations")
        val teacherCourseRelations: MutableList<Teacher>,
//        @ManyToMany(mappedBy = "studentCourseRelations")
//        val studentCourseRelations: MutableList<Student>

) {
    companion object {
        fun create(username: String): Course = Course(
                id = UUID.randomUUID().toString(),
                name = username,
                teacherCourseRelations = mutableListOf(),
//                studentCourseRelations = mutableListOf()
        )
    }

    override fun toString(): String {
        return "Course(id='$id', name='$name')"
    }

}
