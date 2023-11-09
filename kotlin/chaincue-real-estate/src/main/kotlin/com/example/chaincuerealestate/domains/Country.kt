package com.example.chaincuerealestate.domains

import jakarta.persistence.Entity
import jakarta.persistence.Id
import java.util.UUID

@Entity(name = "country")
data class Country(
    @Id
    var id: String,
    var name: String
) {
    companion object {
        fun create(name: CountryNames): Country {
            return Country(
                id = UUID.randomUUID().toString(),
                name = name.toString()
            )
        }
    }

    enum class CountryNames {
        SWEDEN,
        SPAIN
    }
}
