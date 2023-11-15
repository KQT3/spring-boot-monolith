package com.chaincuerealestate.chaincuerealestatereactive.repositories

import com.chaincuerealestate.chaincuerealestatereactive.domains.HouseImages
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface HouseImagesRepository : CoroutineCrudRepository<HouseImages, String>

