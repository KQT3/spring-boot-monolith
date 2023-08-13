package chaincue.tech.jpabackend.masters.teacher_master

import chaincue.tech.jpabackend.masters.user_master.User
import jakarta.persistence.*
import java.util.UUID

@Entity(name = "students")
data class Student(
        @Id
        val id: String,
        val name: String,
        @OneToOne(mappedBy = "student", cascade = [CascadeType.ALL])
        val user: User,
        @ManyToMany(cascade = [CascadeType.PERSIST, CascadeType.MERGE])
        @JoinTable(
                name = "student_course_relations",
                joinColumns = [JoinColumn(name = "student_id")],
                inverseJoinColumns = [JoinColumn(name = "course_id")]
        )
        val studentCourseRelations: MutableList<Course>
) {
    companion object {
        fun create(username: String, user: User): Student = Student(
                id = UUID.randomUUID().toString(),
                name = username,
                user = user,
                mutableListOf()
        )
    }

    override fun toString(): String {
        return "Student(id='$id', name='$name')"
    }


}
