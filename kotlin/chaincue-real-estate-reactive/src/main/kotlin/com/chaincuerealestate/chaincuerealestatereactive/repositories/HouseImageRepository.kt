package com.example.chaincuerealestate.repositories

import com.example.chaincuerealestate.domains.HouseImage
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface HouseImageRepository : CoroutineCrudRepository<HouseImage, String>

