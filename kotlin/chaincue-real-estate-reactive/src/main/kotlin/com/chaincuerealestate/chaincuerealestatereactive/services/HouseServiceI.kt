package com.chaincuerealestate.chaincuerealestatereactive.services

import com.chaincuerealestate.chaincuerealestatereactive.domains.House
import kotlinx.coroutines.flow.Flow

interface HouseServiceI {
    suspend fun save(houseTypes: House.HouseTypes): House
    suspend fun findByIdWithRelations(id: String): House
    suspend fun findAll(): Flow<House>
}

