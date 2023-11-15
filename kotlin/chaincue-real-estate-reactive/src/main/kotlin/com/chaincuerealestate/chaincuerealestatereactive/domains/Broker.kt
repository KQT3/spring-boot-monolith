package com.chaincuerealestate.chaincuerealestatereactive.domains

import org.springframework.data.relational.core.mapping.Table

@Table("broker")
data class Broker(
    val name: String,
    val phoneNumber: String,
    val email: String,
) : AbstractDomain() {

    companion object {
        fun create(email: String): Broker {
            return Broker(
                name = "",
                phoneNumber = "",
                email = email
            )
        }
    }
}
