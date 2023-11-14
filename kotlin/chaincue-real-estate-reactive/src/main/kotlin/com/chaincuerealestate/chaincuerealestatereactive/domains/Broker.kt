package com.chaincuerealestate.chaincuerealestatereactive.domains

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.data.annotation.Id
import org.springframework.data.domain.Persistable
import org.springframework.data.relational.core.mapping.Table
import java.util.*

@Table("broker")
data class Broker(
    @Id
    var id: String,
    var name: String,
    var phoneNumber: String,
    var email: String,
    @Transient private val isNew: Boolean = false
) : Persistable<String> {
    override fun getId() = id

    @JsonIgnore
    override fun isNew() = isNew

    companion object {
        fun create(email: String): Broker {
            return Broker(
                id = UUID.randomUUID().toString(),
                name = "",
                phoneNumber = "",
                email = email
            )
        }
    }
}

