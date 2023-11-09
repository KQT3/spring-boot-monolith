package com.example.chaincuerealestate.services

import com.example.chaincuerealestate.domains.House
import com.example.chaincuerealestate.exceptions.HouseNotFoundException
import com.example.chaincuerealestate.repositories.HouseRepository
import com.example.chaincuerealestate.utilities.AweS3Urls
import org.springframework.stereotype.Service

@Service
class HouseService(private val houseRepository: HouseRepository) : HouseServiceI {

    override fun save(houseTypes: House.HouseTypes): House {
        val house = House.create(houseTypes, AweS3Urls.URLFrontImage1)
        return houseRepository.save(house)
    }

    override fun findById(id: String): House {
        return houseRepository.findById(id)
            .orElseThrow { HouseNotFoundException(id) }
    }

    override fun findAll(): List<House> {
        return houseRepository.findAll()
    }
}

