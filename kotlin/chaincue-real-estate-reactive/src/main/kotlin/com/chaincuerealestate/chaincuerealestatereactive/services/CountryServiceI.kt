package com.chaincuerealestate.chaincuerealestatereactive.services

import com.chaincuerealestate.chaincuerealestatereactive.domains.Country
import kotlinx.coroutines.flow.Flow

interface CountryServiceI {
    suspend fun save(countryNames: Country.CountryNames): Country
    suspend fun findAll(): Flow<Country>
}
