package com.example.chaincuerealestate.domains

import jakarta.persistence.Entity
import jakarta.persistence.Id
import java.util.UUID

@Entity(name = "broker")
data class Broker(
    @Id
    var id: String,
    var name: String,
    var phoneNumber: String,
    var email: String,
) {
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

