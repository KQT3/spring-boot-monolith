package com.example.chaincuerealestate.services

import com.example.chaincuerealestate.domains.House
import kotlinx.coroutines.flow.Flow

interface HouseServiceI {
    suspend fun save(houseTypes: House.HouseTypes): House
    suspend fun findById(id: String): House
    suspend fun findAll(): Flow<House>
}

