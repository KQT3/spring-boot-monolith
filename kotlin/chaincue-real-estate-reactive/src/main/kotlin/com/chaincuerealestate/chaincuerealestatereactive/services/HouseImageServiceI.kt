package com.example.chaincuerealestate.services

import com.example.chaincuerealestate.domains.HouseImage
import kotlinx.coroutines.flow.Flow

interface HouseImageServiceI {
    suspend fun save(url: String): HouseImage
    suspend fun findById(id: String): HouseImage
    suspend fun findAll(): Flow<HouseImage>
}
