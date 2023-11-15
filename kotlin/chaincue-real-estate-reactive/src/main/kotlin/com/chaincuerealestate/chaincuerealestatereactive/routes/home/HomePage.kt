package com.chaincuerealestate.chaincuerealestatereactive.routes.home

import com.chaincuerealestate.chaincuerealestatereactive.domains.Country
import com.chaincuerealestate.chaincuerealestatereactive.domains.House
import com.chaincuerealestate.chaincuerealestatereactive.services.DTOBuilderHelpers.CountryHelper
import com.chaincuerealestate.chaincuerealestatereactive.services.DTOBuilderHelpers.HouseHelper
import kotlinx.coroutines.*
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
    suspend fun homePage(): ResponseEntity<HomePageDTO> {
        log.info("HomePage")
        val homePageDTO = toHomePageDTO { it }
        return ResponseEntity.ok(homePageDTO)
    }

//    private suspend fun toHomePageDTO(additionalProcessing: ((DTOBuilder) -> DTOBuilder)?): HomePageDTO {
//        return (additionalProcessing?.invoke(DTOBuilder()) ?: DTOBuilder())
//            .apply { countryHelper.updateDTOBuilderWithCountries { dtoBuilder: DTOBuilder, countries -> dtoBuilder.countries = countries }.invoke(this) }
//            .apply { houseHelper.updateDTOBuilderWithHouses { dtoBuilder: DTOBuilder, houses -> dtoBuilder.houses = houses }.invoke(this) }
//            .let { toDTO(it) }
//    }

//    private suspend fun toHomePageDTO(additionalProcessing: ((DTOBuilder) -> DTOBuilder)?): HomePageDTO {
//        return coroutineScope {
//            val job = async {
//                (additionalProcessing?.invoke(DTOBuilder()) ?: DTOBuilder())
//                    .apply { countryHelper.updateDTOBuilderWithCountries { dtoBuilder: DTOBuilder, countries -> dtoBuilder.countries = countries }.invoke(this) }
//                    .apply { houseHelper.updateDTOBuilderWithHouses { dtoBuilder: DTOBuilder, houses -> dtoBuilder.houses = houses }.invoke(this) }
//                    .let { toDTO(it) }
//            }
//            job.await()
//        }
//    }

    private suspend fun toHomePageDTO(additionalProcessing: ((DTOBuilder) -> DTOBuilder)?): HomePageDTO {
        return coroutineScope {
            val dtoBuilder = additionalProcessing?.invoke(DTOBuilder()) ?: DTOBuilder()

            val countriesJob = async { countryHelper.updateDTOBuilderWithCountries { dtoBuilder: DTOBuilder, countries -> dtoBuilder.countries = countries } }.await().invoke(dtoBuilder)
            val housesJob = async { houseHelper.updateDTOBuilderWithHouses { dtoBuilder: DTOBuilder, houses -> dtoBuilder.houses = houses } }.await().invoke(dtoBuilder)

//            countriesJob.await().invoke(dtoBuilder)
//            housesJob.await().invoke(dtoBuilder)

            toDTO(dtoBuilder)
        }
    }



    private fun toDTO(dtoBuilder: DTOBuilder): HomePageDTO {
        return HomePageDTO(
            dtoBuilder.countries.map(::toDTO).toTypedArray(),
            dtoBuilder.getHousesSortedByCreated().map(::toDTO).toTypedArray()
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
        fun getHousesSortedByCreated(): List<House> {
            return houses.sortedByDescending { it.created }.take(6).toList()
        }
    }

    private companion object {
        private val log = LoggerFactory.getLogger(HomePage::class.java)
    }
}
