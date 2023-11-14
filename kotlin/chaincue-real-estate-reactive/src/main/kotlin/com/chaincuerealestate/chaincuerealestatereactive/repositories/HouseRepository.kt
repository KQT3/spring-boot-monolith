package com.chaincuerealestate.chaincuerealestatereactive.repositories
import com.chaincuerealestate.chaincuerealestatereactive.domains.House
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface HouseRepository : CoroutineCrudRepository<House, String>

