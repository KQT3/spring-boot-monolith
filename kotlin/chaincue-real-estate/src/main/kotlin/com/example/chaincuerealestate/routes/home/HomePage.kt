package com.example.chaincuerealestate.routes.home

import com.example.chaincuerealestate.domains.Country
import com.example.chaincuerealestate.domains.House
import com.example.chaincuerealestate.services.DTOBuilderHelpers.CountryHelper
import com.example.chaincuerealestate.services.DTOBuilderHelpers.HouseHelper
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("home")
class HomePage(
    private val houseHelper: HouseHelper,
    private val countryHelper: CountryHelper
) {

    @GetMapping
    fun homePage(): ResponseEntity<HomePageDTO> {
        log.info("HomePage")
        val toDTO = toHomePageDTO { it }
        return ResponseEntity.ok(toDTO)
    }

    private fun toHomePageDTO(additionalProcessing: ((DTOBuilder) -> DTOBuilder)?): HomePageDTO {
        return (additionalProcessing?.invoke(DTOBuilder()) ?: DTOBuilder())
            .apply { countryHelper.updateDTOBuilderWithCountries { dtoBuilder: DTOBuilder, countries -> dtoBuilder.countries = countries }.invoke(this) }
            .apply { houseHelper.updateDTOBuilderWithHouses { dtoBuilder: DTOBuilder, houses -> dtoBuilder.houses = houses }.invoke(this) }
            .let { toDTO(it) }
    }

    private fun toDTO(dtoBuilder: DTOBuilder): HomePageDTO {
        return HomePageDTO(
            dtoBuilder.countries.map(::toDTO).toTypedArray(),
            dtoBuilder.getHousesSortedByTimestamp().map(::toDTO).toTypedArray()
        )
    }

    private fun toDTO(country: Country): HomePageDTO.Country {
        return HomePageDTO.Country(
            country.name
        )
    }

    private fun toDTO(house: House): HomePageDTO.House {
        return HomePageDTO.House(
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
    ) {
        fun getHousesSortedByTimestamp(): List<House> {
            return houses.sortedByDescending { it.timestamp }.take(6).toList()
        }
    }

    private companion object {
        private val log = LoggerFactory.getLogger(HomePage::class.java)
    }
}
