package com.chaincuerealestate.chaincuerealestatereactive.services.DTOBuilderHelpers

import com.chaincuerealestate.chaincuerealestatereactive.domains.House
import com.chaincuerealestate.chaincuerealestatereactive.services.HouseService
import kotlinx.coroutines.flow.toList
import org.springframework.stereotype.Component

@Component
class HouseHelper(private val houseService: HouseService) {
    fun <B> updateDTOBuilderWithHouses(setHouses: suspend (B, List<House>) -> Unit): suspend(B) -> B {
        return { dtoBuilder ->
            val houses = houseService.findAll().toList()
            setHouses(dtoBuilder, houses)
            dtoBuilder
        }
    }

    fun <B> updateDTOBuilderWithHouseByHouseId(houseId: String, setHouse: suspend (B, House) -> Unit): suspend (B) -> B {
        return { dtoBuilder ->
            val house = houseService.findById(houseId)
            setHouse(dtoBuilder, house)
            dtoBuilder
        }
    }

    fun <B> updateDTOBuilderWithHouseByHouse(getHouse: suspend (B) -> House, setHouse: (B, House) -> Unit): suspend (B) -> B {
        return { dtoBuilder ->
            val house = houseService.findById(getHouse(dtoBuilder).id)
            setHouse(dtoBuilder, house)
            dtoBuilder
        }
    }

}

