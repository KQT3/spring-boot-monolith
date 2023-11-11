package com.example.chaincuerealestate.services.DTOBuilderHelpers

import com.example.chaincuerealestate.domains.House
import com.example.chaincuerealestate.services.HouseService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class HouseHelper(@Autowired private val houseService: HouseService) {
    fun <B> updateDTOBuilderWithHouses(setHouses: (B, List<House>) -> Unit): (B) -> B {
        return { dtoBuilder ->
            val houses = houseService.findAll()
            setHouses(dtoBuilder, houses)
            dtoBuilder
        }
    }

    fun <B> updateDTOBuilderWithHouseByHouseId(houseId: String, setHouse: (B, House) -> Unit): (B) -> B {
        return { dtoBuilder ->
            val house = houseService.findById(houseId)
            setHouse(dtoBuilder, house)
            dtoBuilder
        }
    }

    fun <B> updateDTOBuilderWithHouseByHouse(getHouse: (B) -> House, setHouse: (B, House) -> Unit): (B) -> B {
        return { dtoBuilder ->
            val house = houseService.findById(getHouse(dtoBuilder).id)
            setHouse(dtoBuilder, house)
            dtoBuilder
        }
    }

}

