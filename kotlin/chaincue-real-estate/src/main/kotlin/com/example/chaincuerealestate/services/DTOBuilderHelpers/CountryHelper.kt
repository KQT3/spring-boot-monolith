package com.example.chaincuerealestate.services.DTOBuilderHelpers

import com.example.chaincuerealestate.domains.Country
import com.example.chaincuerealestate.services.CountryService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.function.BiConsumer

@Component
class CountryHelper(@Autowired private val countryService: CountryService) {
    fun <B> updateDTOBuilderWithCountries(setCountries: BiConsumer<B, List<Country>>): (B) -> B {
        return { dtoBuilder ->
            val countries = countryService.findAll()
            setCountries.accept(dtoBuilder, countries)
            dtoBuilder
        }
    }
}
