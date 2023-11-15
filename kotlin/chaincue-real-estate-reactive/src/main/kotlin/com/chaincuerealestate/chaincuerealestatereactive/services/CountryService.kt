package com.chaincuerealestate.chaincuerealestatereactive.services

import com.chaincuerealestate.chaincuerealestatereactive.domains.Country
import com.chaincuerealestate.chaincuerealestatereactive.repositories.CountryRepository
import kotlinx.coroutines.flow.Flow
import org.springframework.stereotype.Service

@Service
class CountryService(private val countryRepository: CountryRepository) : CountryServiceI {

    override suspend fun save(countryNames: Country.CountryNames): Country {
        val country = Country.create(countryNames)
        return countryRepository.save(country)
    }

    override suspend fun findAll(): Flow<Country> {
        return countryRepository.findAll()
    }
}

