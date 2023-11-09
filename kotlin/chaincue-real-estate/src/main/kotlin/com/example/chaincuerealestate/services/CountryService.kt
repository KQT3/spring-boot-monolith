package com.example.chaincuerealestate.services

import com.example.chaincuerealestate.domains.Country
import com.example.chaincuerealestate.repositories.CountryRepository
import org.springframework.stereotype.Service

@Service
class CountryService(private val countryRepository: CountryRepository) : CountryServiceI {

    override fun save(countryNames: Country.CountryNames): Country {
        val country = Country.create(countryNames)
        return countryRepository.save(country)
    }

    override fun findAll(): List<Country> {
        return countryRepository.findAll()
    }
}

