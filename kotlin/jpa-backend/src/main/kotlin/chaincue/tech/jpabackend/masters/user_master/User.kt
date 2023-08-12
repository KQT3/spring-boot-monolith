package chaincue.tech.jpabackend.masters.user_master

import chaincue.tech.jpabackend.masters.teacher_master.Student
import chaincue.tech.jpabackend.masters.teacher_master.Teacher
import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.OneToOne
import java.util.*

@Entity(name = "users")
data class User(
        @Id
        val id: String,
        val userName: String,
        @OneToOne(cascade = [CascadeType.ALL])
        val teacher: Teacher?,
        @OneToOne(cascade = [CascadeType.ALL])
        val student: Student?,
) {
    companion object {
        fun create(username: String): User = User(
                UUID.randomUUID().toString(),
                userName = username,
                teacher = null,
                student = null
        )
    }
}
