package com.chaincuerealestate.chaincuerealestatereactive.routes.house
import com.chaincuerealestate.chaincuerealestatereactive.domains.Broker
import com.chaincuerealestate.chaincuerealestatereactive.domains.House
import com.chaincuerealestate.chaincuerealestatereactive.domains.HouseImage
import com.chaincuerealestate.chaincuerealestatereactive.routes.home.HomePages
import com.chaincuerealestate.chaincuerealestatereactive.services.DTOBuilderHelpers.HouseHelper
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("house")
class HousePage (
    private val houseHelper: HouseHelper,
) {

    @GetMapping("{houseId}")
    suspend fun homePage(@PathVariable houseId: String): ResponseEntity<HousePageDTO> {
        log.info("HomePage")
        val toDTO = toHomePageDTO(houseId) { it }
        return ResponseEntity.ok(toDTO)
    }

    private suspend fun toHomePageDTO(houseId: String, additionalProcessing: ((DTOBuilder) -> DTOBuilder)?): HousePageDTO {
        return (additionalProcessing?.invoke(DTOBuilder(null)) ?: DTOBuilder(null))
            .apply { houseHelper.updateDTOBuilderWithHouseByHouseId(houseId) { dtoBuilder: DTOBuilder, house -> dtoBuilder.house = house }.invoke(this) }
            .let { toDTO(it) }
    }

    private fun toDTO(dtoBuilder: DTOBuilder): HousePageDTO {
        return HousePageDTO(
            id = dtoBuilder.house!!.id,
            title = dtoBuilder.house!!.title,
            type = dtoBuilder.house!!.houseTypes,
            location = dtoBuilder.house!!.location,
            numberOfRooms = dtoBuilder.house!!.numberRooms,
            beds = dtoBuilder.house!!.beds,
            dollarPrice = dtoBuilder.house!!.price,
            cryptoPrice = "₿32.346",
            description = dtoBuilder.house!!.description,
            images = dtoBuilder.house!!.images.map { toDTO(it) }.toTypedArray(),
            broker = null
        )
    }

    private fun toDTO(country: HouseImage): HousePageDTO.HouseImage {
        return HousePageDTO.HouseImage(
            country.id,
            country.url,
        )
    }

    private fun toDTO(house: Broker): HousePageDTO.Broker {
        return HousePageDTO.Broker(
            house.id!!,
            house.name,
            house.phoneNumber,
            house.email
        )
    }

    private data class DTOBuilder(
        var house: House?
    )

    private companion object {
        private val log = LoggerFactory.getLogger(HomePages::class.java)
    }

}
