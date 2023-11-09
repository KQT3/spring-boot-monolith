package com.example.chaincuerealestate.services

import com.example.chaincuerealestate.domains.Country

interface CountryServiceI {
    fun save(countryNames: Country.CountryNames): Country
    fun findAll(): List<Country>
}
