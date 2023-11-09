package com.example.chaincuerealestate.repositories

import com.example.chaincuerealestate.domains.Country
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface CountryRepository : JpaRepository<Country, String> {
    fun findByName(name: String): Optional<Country>
}

