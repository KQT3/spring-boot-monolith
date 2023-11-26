package com.example.chaincuerealestate.routes.house

import com.example.chaincuerealestate.domains.Broker
import com.example.chaincuerealestate.domains.House
import com.example.chaincuerealestate.domains.HouseImage
import com.example.chaincuerealestate.routes.home.HomePage
import com.example.chaincuerealestate.services.DTOBuilderHelpers.HouseHelper
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
    fun homePage(@PathVariable houseId: String): ResponseEntity<HousePageDTO> {
        log.info("HomePage")
        val toDTO = toHomePageDTO(houseId) { it }
        return ResponseEntity.ok(toDTO)
    }

    private fun toHomePageDTO(houseId: String, additionalProcessing: ((DTOBuilder) -> DTOBuilder)?): HousePageDTO {
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
            broker = dtoBuilder.house!!.broker?.let { toDTO(it) }
        )
    }

    private fun toDTO(country: HouseImage): HousePageDTO.HouseImage {
        return HousePageDTO.HouseImage(
            country.id,
            country.url,
        )
    }

    private fun toDTO(broker: Broker): HousePageDTO.Broker {
        return HousePageDTO.Broker(
            broker.id,
            broker.name,
            broker.phoneNumber,
            broker.email
        )
    }

    private data class DTOBuilder(
        var house: House?
    )

    private companion object {
        private val log = LoggerFactory.getLogger(HomePage::class.java)
    }

}
