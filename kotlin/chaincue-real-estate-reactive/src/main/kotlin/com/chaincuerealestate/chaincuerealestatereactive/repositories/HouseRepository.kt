package com.example.chaincuerealestate.repositories

import com.example.chaincuerealestate.domains.House
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface HouseRepository : CoroutineCrudRepository<House, String>

