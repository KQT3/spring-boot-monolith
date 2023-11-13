package com.example.chaincuerealestate.domains

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.util.*

@Table("country")

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
