package chaincue.tech.jpabackend.masters.teacher_master

import jakarta.persistence.Entity
import jakarta.persistence.Id
import java.util.UUID

@Entity(name = "teachers")
data class Teacher(
        @Id
        val id: String = UUID.randomUUID().toString()
)
