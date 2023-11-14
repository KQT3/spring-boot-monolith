package com.chaincuerealestate.chaincuerealestatereactive.repositories

import com.chaincuerealestate.chaincuerealestatereactive.domains.Country
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface CountryRepository : CoroutineCrudRepository<Country, String> {
    fun findByName(name: String): Optional<Country>
}

