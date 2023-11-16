package com.chaincuerealestate.chaincuerealestatereactive.routes.house

import com.chaincuerealestate.chaincuerealestatereactive.domains.Broker
import com.chaincuerealestate.chaincuerealestatereactive.domains.House
import com.chaincuerealestate.chaincuerealestatereactive.domains.HouseImage
import com.chaincuerealestate.chaincuerealestatereactive.routes.home.HomePage
import com.chaincuerealestate.chaincuerealestatereactive.services.DTOBuilderHelpers.Brokerhelper
import com.chaincuerealestate.chaincuerealestatereactive.services.DTOBuilderHelpers.HouseHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("house")
class HousePage(
    private val houseHelper: HouseHelper,
    private val brokerHelper: Brokerhelper,
) {

    @GetMapping("{houseId}")
    suspend fun housePage(@PathVariable houseId: String): ResponseEntity<HousePageDTO> {
        log.info("housePage")
        val toDTO = toHomePageDTO(houseId) { it }
        return ResponseEntity.ok(toDTO)
    }

    private suspend fun toHomePageDTO(
        houseId: String,
        additionalProcessing: ((DTOBuilder) -> DTOBuilder)?
    ): HousePageDTO {
        return coroutineScope {
            val dtoBuilder = additionalProcessing?.invoke(DTOBuilder()) ?: DTOBuilder()
            val houseJob = async(Dispatchers.IO) {
                houseHelper.updateDTOBuilderWithHouseByHouseId(houseId) { dtoBuilder: DTOBuilder, house ->
                    dtoBuilder.house = house
                }
            }
            val brokers = async(Dispatchers.IO) {
                brokerHelper.updateDTOBuilderWithBrokers { dtoBuilder: DTOBuilder, brokers ->
                    dtoBuilder.brokers = brokers
                }
            }
            houseJob.await().invoke(dtoBuilder)
            brokers.await().invoke(dtoBuilder)
            toDTO(dtoBuilder)
        }
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
            broker = dtoBuilder.getBrokerByHouseId(dtoBuilder.house!!)?.let { toDTO(it) }
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
        var house: House? = null,
        var brokers: List<Broker> = listOf()
    ) {
        fun getBrokerByHouseId(house: House): Broker? {
            return brokers.find { it.id == house.brokerId }
        }
    }

    private companion object {
        private val log = LoggerFactory.getLogger(HomePage::class.java)
    }

}
