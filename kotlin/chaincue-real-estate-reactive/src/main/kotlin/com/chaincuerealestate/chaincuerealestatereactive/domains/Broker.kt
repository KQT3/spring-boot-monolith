package com.example.chaincuerealestate.domains

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.util.*

@Table("broker")
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

