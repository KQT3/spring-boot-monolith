package com.example.chaincuerealestate.routes.houses

import com.example.chaincuerealestate.domains.Country
import com.example.chaincuerealestate.domains.House
import com.example.chaincuerealestate.routes.home.HomePage
import com.example.chaincuerealestate.services.DTOBuilderHelpers.CountryHelper
import com.example.chaincuerealestate.services.DTOBuilderHelpers.HouseHelper
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("houses")
class HousesPage (
    private val houseHelper: HouseHelper,
    private val countryHelper: CountryHelper
) {

    @GetMapping
    fun homePage(): ResponseEntity<HousesPageDTO> {
        log.info("HomePage")
        val toDTO = toHomePageDTO { it }
        return ResponseEntity.ok(toDTO)
    }

    private fun toHomePageDTO(additionalProcessing: ((DTOBuilder) -> DTOBuilder)?): HousesPageDTO {
        return (additionalProcessing?.invoke(DTOBuilder()) ?: DTOBuilder())
            .apply { countryHelper.updateDTOBuilderWithCountries { dtoBuilder: DTOBuilder, countries -> dtoBuilder.countries = countries }.invoke(this) }
            .apply { houseHelper.updateDTOBuilderWithHouses { dtoBuilder: DTOBuilder, houses -> dtoBuilder.houses = houses }.invoke(this) }
            .let { toDTO(it) }
    }

    private fun toDTO(dtoBuilder: DTOBuilder): HousesPageDTO {
        return HousesPageDTO(
            dtoBuilder.countries.map(::toDTO).toTypedArray(),
            dtoBuilder.houses.map(::toDTO).toTypedArray()
        )
    }

    private fun toDTO(country: Country): HousesPageDTO.Country {
        return HousesPageDTO.Country(
            country.name
        )
    }

    private fun toDTO(house: House): HousesPageDTO.House {
        return HousesPageDTO.House(
            house.id,
            house.title,
            house.location,
            house.numberRooms,
            house.beds,
            house.price,
            "₿32.346",
            house.src
        )
    }

    private data class DTOBuilder(
        var countries: List<Country> = ArrayList(),
        var houses: List<House> = ArrayList()
    )

    private companion object {
        private val log = LoggerFactory.getLogger(HomePage::class.java)
    }
}
