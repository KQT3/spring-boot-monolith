package com.chaincuerealestate.chaincuerealestatereactive.domains

import org.springframework.data.relational.core.mapping.Table

@Table("country")

data class Country(
    var name: String
) : AbstractDomain() {
    companion object {
        fun create(name: CountryNames): Country {
            return Country(
                name = name.toString()
            )
        }
    }

    enum class CountryNames {
        SWEDEN,
        SPAIN
    }

}
