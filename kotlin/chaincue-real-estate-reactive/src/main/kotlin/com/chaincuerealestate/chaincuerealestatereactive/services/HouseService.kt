package com.example.chaincuerealestate.services

import com.example.chaincuerealestate.domains.House
import com.example.chaincuerealestate.exceptions.HouseNotFoundException
import com.example.chaincuerealestate.repositories.HouseRepository
import com.example.chaincuerealestate.utilities.AweS3Urls
import kotlinx.coroutines.flow.Flow
import org.springframework.stereotype.Service

@Service
class HouseService(private val houseRepository: HouseRepository) : HouseServiceI {

    override suspend fun save(houseTypes: House.HouseTypes): House {
        val house = House.create(houseTypes, AweS3Urls.URLFrontImage1)
        return houseRepository.save(house)
    }

    override suspend fun findById(id: String): House {
        return houseRepository.findById(id) ?: throw HouseNotFoundException(id)
    }

    override suspend fun findAll(): Flow<House> {
        return houseRepository.findAll()
    }
}

