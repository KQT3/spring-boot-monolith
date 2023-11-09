package com.example.chaincuerealestate.repositories

import com.example.chaincuerealestate.domains.HouseImage
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface HouseImageRepository : JpaRepository<HouseImage, String>

