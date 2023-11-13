package com.example.chaincuerealestate.services

import com.example.chaincuerealestate.domains.Country
import kotlinx.coroutines.flow.Flow

interface CountryServiceI {
    suspend fun save(countryNames: Country.CountryNames): Country
    suspend fun findAll(): Flow<Country>
}
