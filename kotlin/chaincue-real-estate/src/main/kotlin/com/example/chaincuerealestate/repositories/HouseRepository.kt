package com.example.chaincuerealestate.repositories

import com.example.chaincuerealestate.domains.House
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface HouseRepository : JpaRepository<House, String>

