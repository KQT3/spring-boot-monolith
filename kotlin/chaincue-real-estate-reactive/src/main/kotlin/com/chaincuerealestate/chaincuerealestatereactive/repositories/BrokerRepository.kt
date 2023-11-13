package com.example.chaincuerealestate.repositories

import com.example.chaincuerealestate.domains.Broker
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface BrokerRepository : CoroutineCrudRepository<Broker, String>

