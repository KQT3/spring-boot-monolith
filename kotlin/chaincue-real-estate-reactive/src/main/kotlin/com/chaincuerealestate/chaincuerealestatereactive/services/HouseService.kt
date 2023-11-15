package com.chaincuerealestate.chaincuerealestatereactive.services

import com.chaincuerealestate.chaincuerealestatereactive.domains.House
import com.chaincuerealestate.chaincuerealestatereactive.exceptions.HouseNotFoundException
import com.chaincuerealestate.chaincuerealestatereactive.repositories.HouseRepository
import com.chaincuerealestate.chaincuerealestatereactive.utilities.AweS3Urls
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
        val findAll = houseRepository.findAll()
        return findAll
    }
}

