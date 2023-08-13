package chaincue.tech.jpabackend.masters.teacher_master

import chaincue.tech.jpabackend.masters.user_master.User
import jakarta.persistence.*
import lombok.EqualsAndHashCode
import lombok.ToString
import java.util.UUID

@Entity(name = "teachers")
data class Teacher(
        @Id
        val id: String,
        val name: String,
        @OneToOne(mappedBy = "teacher", cascade = [CascadeType.ALL])
        val user: User,
        @ManyToMany(cascade = [CascadeType.PERSIST, CascadeType.MERGE])
        @JoinTable(
                name = "teacher_course_relations",
                joinColumns = [JoinColumn(name = "teacher_id")],
                inverseJoinColumns = [JoinColumn(name = "course_id")]
        )
        val teacherCourseRelations: MutableList<Course>
) {
    companion object {
        fun create(username: String, user: User): Teacher = Teacher(
                id = UUID.randomUUID().toString(),
                name = username,
                user = user,
                teacherCourseRelations = mutableListOf()
        )
    }

    override fun toString(): String {
        return "Teacher(id='$id', name='$name')"
    }


}
