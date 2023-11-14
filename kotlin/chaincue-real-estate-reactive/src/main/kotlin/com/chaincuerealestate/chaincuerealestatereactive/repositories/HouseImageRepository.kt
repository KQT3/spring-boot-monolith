package com.chaincuerealestate.chaincuerealestatereactive.repositories

import com.chaincuerealestate.chaincuerealestatereactive.domains.HouseImage
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface HouseImageRepository : CoroutineCrudRepository<HouseImage, String>

