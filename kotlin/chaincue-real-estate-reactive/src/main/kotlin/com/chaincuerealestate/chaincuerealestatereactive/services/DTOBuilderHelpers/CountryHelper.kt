package com.chaincuerealestate.chaincuerealestatereactive.services.DTOBuilderHelpers

import com.chaincuerealestate.chaincuerealestatereactive.domains.Country
import com.chaincuerealestate.chaincuerealestatereactive.services.CountryService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.toList
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class CountryHelper(private val countryService: CountryService) {

    fun <B> updateDTOBuilderWithCountries(setCountries: suspend (B, List<Country>) -> Unit): suspend (B) -> B {
        return { dtoBuilder ->
            val countries = countryService.findAll().toList()
            setCountries(dtoBuilder, countries)
            dtoBuilder
        }
    }

}
