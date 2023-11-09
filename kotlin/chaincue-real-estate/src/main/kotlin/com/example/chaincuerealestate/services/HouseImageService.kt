package com.example.chaincuerealestate.services

import com.example.chaincuerealestate.domains.HouseImage
import com.example.chaincuerealestate.exceptions.HouseImageNotFoundException
import com.example.chaincuerealestate.repositories.HouseImageRepository
import org.springframework.stereotype.Service

@Service
class HouseImageService(private val houseImageRepository: HouseImageRepository) : HouseImageServiceI {

    override fun save(url: String): HouseImage {
        val houseImage = HouseImage.create(url)
        return houseImageRepository.save(houseImage)
    }

    override fun findById(id: String): HouseImage {
        return houseImageRepository.findById(id)
            .orElseThrow { HouseImageNotFoundException(id) }
    }

    override fun findAll(): List<HouseImage> {
        return houseImageRepository.findAll()
    }
}
