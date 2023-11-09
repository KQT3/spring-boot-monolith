package com.example.chaincuerealestate.services.DTOBuilderHelpers

import com.example.chaincuerealestate.domains.House
import com.example.chaincuerealestate.services.HouseService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.function.BiConsumer
import java.util.function.Function

@Component
class HouseHelper(@Autowired private val houseService: HouseService) {
    fun <B> updateDTOBuilderWithHouseByHouse(getHouse: Function<B, House>, setHouse: BiConsumer<B, House>): (B) -> B {
        return { dtoBuilder ->
            val house = houseService.findById(getHouse.apply(dtoBuilder).id)
            setHouse.accept(dtoBuilder, house)
            dtoBuilder
        }
    }

    fun <B> updateDTOBuilderWithHouses(setHouses: BiConsumer<B, List<House>>): (B) -> B {
        return { dtoBuilder ->
            val houses = houseService.findAll()
            setHouses.accept(dtoBuilder, houses)
            dtoBuilder
        }
    }

    fun <B> updateDTOBuilderWithHouseByHouseId(houseId: String, setHouse: BiConsumer<B, House>): (B) -> B {
        return { dtoBuilder ->
            val house = houseService.findById(houseId)
            setHouse.accept(dtoBuilder, house)
            dtoBuilder
        }
    }
}

