package com.chaincuerealestate.chaincuerealestatereactive.repositories

import com.chaincuerealestate.chaincuerealestatereactive.domains.HouseImageRelations
import kotlinx.coroutines.flow.Flow
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface HouseImagesRepository : CoroutineCrudRepository<HouseImageRelations, String> {
    fun findAllByHouseId(name: String): Flow<HouseImageRelations>
}
