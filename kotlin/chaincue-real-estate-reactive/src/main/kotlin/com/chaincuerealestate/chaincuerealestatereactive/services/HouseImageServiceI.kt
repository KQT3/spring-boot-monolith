package com.chaincuerealestate.chaincuerealestatereactive.services

import com.chaincuerealestate.chaincuerealestatereactive.domains.HouseImage
import kotlinx.coroutines.flow.Flow

interface HouseImageServiceI {
    suspend fun save(url: String): HouseImage
    suspend fun findById(id: String): HouseImage
    suspend fun findAll(): Flow<HouseImage>
}
