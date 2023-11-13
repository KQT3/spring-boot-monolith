package com.example.chaincuerealestate.services

import com.example.chaincuerealestate.domains.HouseImage
import com.example.chaincuerealestate.exceptions.HouseImageNotFoundException
import com.example.chaincuerealestate.repositories.HouseImageRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import org.springframework.stereotype.Service

@Service
class HouseImageService(private val houseImageRepository: HouseImageRepository) : HouseImageServiceI {

    override suspend fun save(url: String): HouseImage {
        val houseImage = HouseImage.create(url)
        return houseImageRepository.save(houseImage)
    }

    override suspend fun findById(id: String): HouseImage {
        return withContext(Dispatchers.IO) {
            houseImageRepository.findById(id) ?: throw HouseImageNotFoundException(id)
        }
    }

    override suspend fun findAll(): Flow<HouseImage> {
        return houseImageRepository.findAll()
    }
}
